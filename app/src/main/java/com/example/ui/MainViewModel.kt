package com.example.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AlertCategory
import com.example.data.AlertPreferencesStore
import com.example.data.AppTab
import com.example.data.BookmarkType
import com.example.data.CustomerReview
import com.example.data.GalleryHighlight
import com.example.data.InquiryFormState
import com.example.data.JobAlertItem
import com.example.data.LocalShopRepository
import com.example.data.SavedBookmark
import com.example.data.SelectedServiceContext
import com.example.data.ServiceCategory
import com.example.data.ServiceInquiry
import com.example.data.ShopCatalogRepository
import com.example.data.ShopCloudStore
import com.example.data.StationeryCategory
import com.example.data.TickerHeadline
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface CloudUiState<out T> {
  data object Loading : CloudUiState<Nothing>

  data class Success<T>(val data: T) : CloudUiState<T>

  data class Error(val message: String) : CloudUiState<Nothing>
}

data class ShopUiState(
  val currentUserId: String = "",
  val userDisplayName: String = "",
  val selectedTab: AppTab = AppTab.HOME,
  val serviceSearchQuery: String = "",
  val expandedCategoryIds: Set<Int> = setOf(1),
  val allCategories: List<ServiceCategory> = ShopCatalogRepository.serviceCategories,
  val filteredCategories: List<ServiceCategory> = ShopCatalogRepository.serviceCategories,
  val selectedServiceSheet: SelectedServiceContext? = null,
  val selectedAlertCategory: AlertCategory = AlertCategory.ALL,
  val filteredAlerts: List<JobAlertItem> = ShopCatalogRepository.jobAlerts,
  val alertsEnabled: Boolean = false,
  val stationeryCategories: List<StationeryCategory> = ShopCatalogRepository.stationeryCategories,
  val galleryHighlights: List<GalleryHighlight> = ShopCatalogRepository.galleryHighlights,
  val customerReviews: List<CustomerReview> = ShopCatalogRepository.customerReviews,
  val tickerHeadlines: List<TickerHeadline> = ShopCatalogRepository.tickerHeadlines,
  val inquiryForm: InquiryFormState = InquiryFormState(),
  val inquiriesState: CloudUiState<List<ServiceInquiry>> = CloudUiState.Loading,
  val bookmarksState: CloudUiState<List<SavedBookmark>> = CloudUiState.Loading,
  val cloudFeedbackMessage: String? = null,
)

private data class InternalUiState(
  val selectedTab: AppTab = AppTab.HOME,
  val serviceSearchQuery: String = "",
  val expandedCategoryIds: Set<Int> = setOf(1),
  val selectedServiceSheet: SelectedServiceContext? = null,
  val selectedAlertCategory: AlertCategory = AlertCategory.ALL,
  val inquiryForm: InquiryFormState = InquiryFormState(),
  val cloudFeedbackMessage: String? = null,
)

class MainViewModel(
  private val alertPrefsRepo: AlertPreferencesStore,
  private val cloudStore: ShopCloudStore,
  private val currentUserId: String,
  private val userDisplayName: String = "Customer",
  private val userEmail: String? = null,
) : ViewModel() {

  private val internalState =
    MutableStateFlow(
      InternalUiState(
        inquiryForm =
          InquiryFormState(
            name = if (userDisplayName != "Customer") userDisplayName else ""
          )
      )
    )

  private val inquiriesFlow: StateFlow<CloudUiState<List<ServiceInquiry>>> =
    cloudStore
      .observeInquiries(currentUserId)
      .map<List<ServiceInquiry>, CloudUiState<List<ServiceInquiry>>> { CloudUiState.Success(it) }
      .catch { error ->
        if (error is CancellationException) throw error
        Log.w(TAG, "Error observing inquiries", error)
        emit(CloudUiState.Error(error.localizedMessage ?: "Failed to load saved inquiries."))
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = CloudUiState.Loading,
      )

  private val bookmarksFlow: StateFlow<CloudUiState<List<SavedBookmark>>> =
    cloudStore
      .observeBookmarks(currentUserId)
      .map<List<SavedBookmark>, CloudUiState<List<SavedBookmark>>> { CloudUiState.Success(it) }
      .catch { error ->
        if (error is CancellationException) throw error
        Log.w(TAG, "Error observing bookmarks", error)
        emit(CloudUiState.Error(error.localizedMessage ?: "Failed to load bookmarks."))
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = CloudUiState.Loading,
      )

  private val profileAlertsFlow =
    cloudStore
      .observeUserProfile(currentUserId)
      .map { it?.alertsEnabled }
      .catch { error ->
        if (error is CancellationException) throw error
        Log.w(TAG, "Error observing user profile", error)
        emit(null)
      }

  val uiState: StateFlow<ShopUiState> =
    combine(
        internalState,
        alertPrefsRepo.alertsEnabled,
        profileAlertsFlow,
        inquiriesFlow,
        bookmarksFlow,
      ) { state, localAlerts, cloudAlerts, inquiries, bookmarks ->
        val query = state.serviceSearchQuery.trim()
        val categories =
          if (query.isEmpty()) {
            ShopCatalogRepository.serviceCategories
          } else {
            ShopCatalogRepository.serviceCategories.mapNotNull { cat ->
              val matchesHeader =
                cat.englishTitle.contains(query, ignoreCase = true) ||
                  cat.hindiTitle.contains(query, ignoreCase = true)
              val matchingServices =
                cat.services.filter { it.contains(query, ignoreCase = true) }
              when {
                matchesHeader -> cat
                matchingServices.isNotEmpty() -> cat.copy(services = matchingServices)
                else -> null
              }
            }
          }

        val expandedIds =
          if (query.isNotEmpty()) {
            categories.map { it.id }.toSet()
          } else {
            state.expandedCategoryIds
          }

        val alerts =
          if (state.selectedAlertCategory == AlertCategory.ALL) {
            ShopCatalogRepository.jobAlerts
          } else {
            ShopCatalogRepository.jobAlerts.filter { it.category == state.selectedAlertCategory }
          }

        ShopUiState(
          currentUserId = currentUserId,
          userDisplayName = userDisplayName,
          selectedTab = state.selectedTab,
          serviceSearchQuery = state.serviceSearchQuery,
          expandedCategoryIds = expandedIds,
          allCategories = ShopCatalogRepository.serviceCategories,
          filteredCategories = categories,
          selectedServiceSheet = state.selectedServiceSheet,
          selectedAlertCategory = state.selectedAlertCategory,
          filteredAlerts = alerts,
          alertsEnabled = cloudAlerts ?: localAlerts,
          inquiryForm = state.inquiryForm,
          inquiriesState = inquiries,
          bookmarksState = bookmarks,
          cloudFeedbackMessage = state.cloudFeedbackMessage,
        )
      }
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = ShopUiState(currentUserId = currentUserId, userDisplayName = userDisplayName),
      )

  init {
    viewModelScope.launch {
      val localDefault = alertPrefsRepo.alertsEnabled.first()
      cloudStore.ensureUserProfile(currentUserId, userDisplayName, userEmail, localDefault)
    }
  }

  fun selectTab(tab: AppTab) {
    internalState.update { it.copy(selectedTab = tab) }
  }

  fun jumpToAlerts(category: AlertCategory = AlertCategory.ALL) {
    internalState.update {
      it.copy(selectedTab = AppTab.ALERTS, selectedAlertCategory = category)
    }
  }

  fun updateServiceSearch(query: String) {
    internalState.update { it.copy(serviceSearchQuery = query) }
  }

  fun toggleCategory(categoryId: Int) {
    internalState.update { state ->
      val next =
        if (categoryId in state.expandedCategoryIds) {
          state.expandedCategoryIds - categoryId
        } else {
          state.expandedCategoryIds + categoryId
        }
      state.copy(expandedCategoryIds = next)
    }
  }

  fun toggleExpandAll(expandAll: Boolean) {
    internalState.update { state ->
      val ids =
        if (expandAll) {
          ShopCatalogRepository.serviceCategories.map { it.id }.toSet()
        } else {
          emptySet()
        }
      state.copy(expandedCategoryIds = ids)
    }
  }

  fun openServiceSheet(categoryTitle: String, serviceName: String) {
    internalState.update {
      it.copy(selectedServiceSheet = SelectedServiceContext(categoryTitle, serviceName))
    }
  }

  fun dismissServiceSheet() {
    internalState.update { it.copy(selectedServiceSheet = null) }
  }

  fun selectAlertCategory(category: AlertCategory) {
    internalState.update { it.copy(selectedAlertCategory = category) }
  }

  fun setAlertsEnabled(enabled: Boolean) {
    viewModelScope.launch {
      alertPrefsRepo.setAlertsEnabled(enabled)
      val res = cloudStore.updateAlertsPreference(currentUserId, enabled)
      if (res.isFailure) {
        internalState.update {
          it.copy(cloudFeedbackMessage = "Saved locally; cloud sync will retry when online.")
        }
      }
    }
  }

  fun toggleServiceBookmark(categoryTitle: String, serviceName: String, currentlySaved: Boolean) {
    viewModelScope.launch {
      val result =
        cloudStore.toggleBookmark(
          userId = currentUserId,
          rawKey = "${categoryTitle}_$serviceName",
          itemTitle = serviceName,
          categoryTitle = categoryTitle,
          itemType = BookmarkType.SERVICE,
          currentlySaved = currentlySaved,
        )
      internalState.update {
        it.copy(
          cloudFeedbackMessage =
            if (result.isSuccess) {
              if (currentlySaved) "Removed from saved services." else "Saved to your bookmarks!"
            } else {
              "Could not update bookmark. Please check your connection."
            }
        )
      }
    }
  }

  fun toggleAlertBookmark(alert: JobAlertItem, currentlySaved: Boolean) {
    viewModelScope.launch {
      val result =
        cloudStore.toggleBookmark(
          userId = currentUserId,
          rawKey = alert.id,
          itemTitle = alert.title,
          categoryTitle = alert.badgeLabel,
          itemType = BookmarkType.ALERT,
          currentlySaved = currentlySaved,
        )
      internalState.update {
        it.copy(
          cloudFeedbackMessage =
            if (result.isSuccess) {
              if (currentlySaved) "Removed alert bookmark." else "Alert saved to your bookmarks!"
            } else {
              "Could not update bookmark. Please check your connection."
            }
        )
      }
    }
  }

  fun removeBookmark(bookmarkId: String) {
    viewModelScope.launch {
      val result = cloudStore.removeBookmark(currentUserId, bookmarkId)
      internalState.update {
        it.copy(
          cloudFeedbackMessage =
            if (result.isSuccess) {
              "Removed from bookmarks."
            } else {
              "Could not remove bookmark. Please check your connection."
            }
        )
      }
    }
  }

  fun isBookmarked(itemType: BookmarkType, rawKey: String): Boolean {
    val state = uiState.value.bookmarksState
    if (state !is CloudUiState.Success) return false
    val expectedId = LocalShopRepository.sanitizeBookmarkId(itemType, rawKey)
    return state.data.any { it.id == expectedId }
  }

  fun prefillInquiryFromService(serviceName: String) {
    internalState.update { state ->
      state.copy(
        selectedServiceSheet = null,
        selectedTab = AppTab.CONTACT,
        inquiryForm =
          state.inquiryForm.copy(
            selectedService = serviceName,
            message = "I need help with: $serviceName",
            isSubmitted = false,
          ),
      )
    }
  }

  fun updateInquiryName(name: String) {
    internalState.update {
      it.copy(
        inquiryForm = it.inquiryForm.copy(name = name, nameError = null, isSubmitted = false)
      )
    }
  }

  fun updateInquiryPhone(phone: String) {
    internalState.update {
      it.copy(
        inquiryForm = it.inquiryForm.copy(phone = phone, phoneError = null, isSubmitted = false)
      )
    }
  }

  fun updateInquiryService(service: String) {
    internalState.update {
      it.copy(inquiryForm = it.inquiryForm.copy(selectedService = service, isSubmitted = false))
    }
  }

  fun updateInquiryMessage(message: String) {
    internalState.update {
      it.copy(inquiryForm = it.inquiryForm.copy(message = message, isSubmitted = false))
    }
  }

  /**
   * Validates the inquiry form, persists it to Cloud Firestore, and returns the pre-filled
   * WhatsApp message if valid, or null if validation failed.
   */
  fun validateAndBuildWhatsAppInquiry(): String? {
    val form = internalState.value.inquiryForm
    val trimmedName = form.name.trim()
    val trimmedPhone = form.phone.trim()

    val nameErr =
      if (trimmedName.length < 2) "Please enter at least 2 characters for your name" else null
    val digitsOnly = trimmedPhone.filter { it.isDigit() }
    val phoneErr = if (digitsOnly.length < 10) "Enter a valid 10-digit mobile number" else null

    if (nameErr != null || phoneErr != null) {
      internalState.update {
        it.copy(inquiryForm = form.copy(nameError = nameErr, phoneError = phoneErr))
      }
      return null
    }

    val cleanMessage = form.message.trim().take(500)
    internalState.update {
      it.copy(inquiryForm = form.copy(nameError = null, phoneError = null, isSubmitted = true))
    }

    viewModelScope.launch {
      val result =
        cloudStore.submitInquiry(
          userId = currentUserId,
          customerName = trimmedName,
          phone = trimmedPhone,
          serviceName = form.selectedService,
          message = cleanMessage,
        )
      if (result.isFailure) {
        internalState.update {
          it.copy(cloudFeedbackMessage = "Failed to save inquiry to cloud. Please try again.")
        }
      }
    }

    val detail = cleanMessage.ifBlank { "Please share timing & required documents." }
    return buildString {
      append("Namaste Pradeep ji (R K Gallery & Stationers),\n")
      append("Name: $trimmedName\n")
      append("Phone: $trimmedPhone\n")
      append("Service: ${form.selectedService}\n")
      append("Details: $detail")
    }
  }

  fun markInquiryCompleted(inquiryId: String) {
    viewModelScope.launch {
      val result = cloudStore.markInquiryCompleted(currentUserId, inquiryId)
      if (result.isFailure) {
        internalState.update {
          it.copy(cloudFeedbackMessage = "Could not update inquiry status.")
        }
      }
    }
  }

  fun deleteInquiry(inquiryId: String) {
    viewModelScope.launch {
      val result = cloudStore.deleteInquiry(currentUserId, inquiryId)
      if (result.isFailure) {
        internalState.update {
          it.copy(cloudFeedbackMessage = "Could not delete inquiry.")
        }
      }
    }
  }

  fun resetInquiryConfirmation() {
    internalState.update {
      it.copy(
        inquiryForm =
          InquiryFormState(
            name = if (userDisplayName != "Customer") userDisplayName else ""
          )
      )
    }
  }

  fun clearCloudFeedback() {
    internalState.update { it.copy(cloudFeedbackMessage = null) }
  }

  class Factory(
    private val alertPrefsRepo: AlertPreferencesStore,
    private val cloudStore: ShopCloudStore,
    private val currentUserId: String,
    private val userDisplayName: String,
    private val userEmail: String?,
  ) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return MainViewModel(
        alertPrefsRepo = alertPrefsRepo,
        cloudStore = cloudStore,
        currentUserId = currentUserId,
        userDisplayName = userDisplayName,
        userEmail = userEmail,
      ) as T
    }
  }

  private companion object {
    const val TAG = "MainViewModel"
    const val STOP_TIMEOUT_MILLIS = 5000L
  }
}
