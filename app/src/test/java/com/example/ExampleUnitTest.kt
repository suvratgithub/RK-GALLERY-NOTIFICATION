package com.example

import com.example.data.AlertCategory
import com.example.data.AlertPreferencesStore
import com.example.data.AppTab
import com.example.data.BookmarkType
import com.example.data.InquiryStatus
import com.example.data.SavedBookmark
import com.example.data.ServiceInquiry
import com.example.data.ShopCatalogRepository
import com.example.data.LocalShopRepository
import com.example.data.ShopCloudStore
import com.example.data.UserProfile
import com.example.ui.CloudUiState
import com.example.ui.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExampleUnitTest {

  private class FakeAlertPreferencesStore : AlertPreferencesStore {
    private val state = MutableStateFlow(false)
    override val alertsEnabled: Flow<Boolean> = state

    override suspend fun setAlertsEnabled(enabled: Boolean) {
      state.value = enabled
    }
  }

  private class FakeShopCloudStore : ShopCloudStore {
    private val profileFlow = MutableStateFlow<UserProfile?>(null)
    private val inquiriesFlow = MutableStateFlow<List<ServiceInquiry>>(emptyList())
    private val bookmarksFlow = MutableStateFlow<List<SavedBookmark>>(emptyList())
    private var nextInquirySeq = 1

    override suspend fun ensureUserProfile(
      userId: String,
      displayName: String,
      email: String?,
      defaultAlertsEnabled: Boolean,
    ): Result<Unit> {
      if (profileFlow.value == null) {
        profileFlow.value =
          UserProfile(
            id = userId,
            userId = userId,
            displayName = displayName,
            email = email,
            alertsEnabled = defaultAlertsEnabled,
          )
      }
      return Result.success(Unit)
    }

    override fun observeUserProfile(userId: String): Flow<UserProfile?> =
      profileFlow.map { if (it?.userId == userId) it else null }

    override suspend fun updateAlertsPreference(userId: String, enabled: Boolean): Result<Unit> {
      val current = profileFlow.value
      if (current != null && current.userId == userId) {
        profileFlow.value = current.copy(alertsEnabled = enabled)
      }
      return Result.success(Unit)
    }

    override fun observeInquiries(userId: String): Flow<List<ServiceInquiry>> =
      inquiriesFlow.map { list -> list.filter { it.userId == userId } }

    override suspend fun submitInquiry(
      userId: String,
      customerName: String,
      phone: String,
      serviceName: String,
      message: String,
    ): Result<Unit> {
      val newInquiry =
        ServiceInquiry(
          id = "inq_${nextInquirySeq++}",
          userId = userId,
          customerName = customerName,
          phone = phone,
          serviceName = serviceName,
          message = message,
          status = InquiryStatus.OPEN.name,
        )
      inquiriesFlow.value = listOf(newInquiry) + inquiriesFlow.value
      return Result.success(Unit)
    }

    override suspend fun markInquiryCompleted(userId: String, inquiryId: String): Result<Unit> {
      inquiriesFlow.value =
        inquiriesFlow.value.map {
          if (it.id == inquiryId && it.userId == userId) {
            it.copy(status = InquiryStatus.COMPLETED.name)
          } else {
            it
          }
        }
      return Result.success(Unit)
    }

    override suspend fun deleteInquiry(userId: String, inquiryId: String): Result<Unit> {
      inquiriesFlow.value =
        inquiriesFlow.value.filterNot { it.id == inquiryId && it.userId == userId }
      return Result.success(Unit)
    }

    override fun observeBookmarks(userId: String): Flow<List<SavedBookmark>> =
      bookmarksFlow.map { list -> list.filter { it.userId == userId } }

    override suspend fun toggleBookmark(
      userId: String,
      rawKey: String,
      itemTitle: String,
      categoryTitle: String,
      itemType: BookmarkType,
      currentlySaved: Boolean,
    ): Result<Unit> {
      val docId = LocalShopRepository.sanitizeBookmarkId(itemType, rawKey)
      bookmarksFlow.value =
        if (currentlySaved) {
          bookmarksFlow.value.filterNot { it.id == docId && it.userId == userId }
        } else {
          val bookmark =
            SavedBookmark(
              id = docId,
              userId = userId,
              itemTitle = itemTitle,
              categoryTitle = categoryTitle,
              itemType = itemType.name,
            )
          listOf(bookmark) + bookmarksFlow.value.filterNot { it.id == docId }
        }
      return Result.success(Unit)
    }

    override suspend fun removeBookmark(userId: String, bookmarkId: String): Result<Unit> {
      bookmarksFlow.value =
        bookmarksFlow.value.filterNot { it.id == bookmarkId && it.userId == userId }
      return Result.success(Unit)
    }
  }

  private val testDispatcher = UnconfinedTestDispatcher()

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  private fun createViewModel(
    prefs: FakeAlertPreferencesStore = FakeAlertPreferencesStore(),
    cloud: FakeShopCloudStore = FakeShopCloudStore(),
  ): MainViewModel =
    MainViewModel(
      alertPrefsRepo = prefs,
      cloudStore = cloud,
      currentUserId = "uid_test_123",
      userDisplayName = "Rahul Sharma",
      userEmail = "rahul@example.com",
    )

  @Test
  fun catalogHas20CategoriesAndInstagramUrl() {
    assertEquals(20, ShopCatalogRepository.serviceCategories.size)
    assertTrue(
      ShopCatalogRepository.INSTAGRAM_URL.startsWith(
        "https://www.instagram.com/rk_gallery_and_stationery_jbp"
      )
    )
    assertEquals("@rk_gallery_and_stationery_jbp", ShopCatalogRepository.INSTAGRAM_HANDLE)
  }

  @Test
  fun searchFiltersBothEnglishAndHindiTags() = runTest {
    val vm = createViewModel()
    val job = launch(testDispatcher) { vm.uiState.collect() }

    vm.updateServiceSearch("आधार")
    assertTrue(vm.uiState.value.filteredCategories.isNotEmpty())
    assertTrue(vm.uiState.value.filteredCategories.any { it.id == 2 })

    vm.updateServiceSearch("Passport")
    assertTrue(vm.uiState.value.filteredCategories.any { it.id == 17 })

    job.cancel()
  }

  @Test
  fun inquiryValidationPersistsCompletesAndDeletesInCloud() = runTest {
    val vm = createViewModel()
    val job = launch(testDispatcher) { vm.uiState.collect() }

    vm.updateInquiryName("")
    assertNull(vm.validateAndBuildWhatsAppInquiry())
    assertNotNull(vm.uiState.value.inquiryForm.nameError)
    assertNotNull(vm.uiState.value.inquiryForm.phoneError)
    assertFalse(vm.uiState.value.inquiryForm.isSubmitted)

    vm.updateInquiryName("Rahul")
    vm.updateInquiryPhone("9179818444")
    val message = vm.validateAndBuildWhatsAppInquiry()
    assertNotNull(message)
    assertTrue(message!!.contains("Rahul"))
    assertTrue(vm.uiState.value.inquiryForm.isSubmitted)

    val inquiries = (vm.uiState.value.inquiriesState as CloudUiState.Success).data
    assertEquals(1, inquiries.size)
    assertEquals(InquiryStatus.OPEN.name, inquiries.first().status)

    vm.markInquiryCompleted(inquiries.first().id)
    val updated = (vm.uiState.value.inquiriesState as CloudUiState.Success).data
    assertEquals(InquiryStatus.COMPLETED.name, updated.first().status)

    vm.deleteInquiry(inquiries.first().id)
    val afterDelete = (vm.uiState.value.inquiriesState as CloudUiState.Success).data
    assertTrue(afterDelete.isEmpty())

    job.cancel()
  }

  @Test
  fun bookmarksToggleAndRemoveById() = runTest {
    val vm = createViewModel()
    val job = launch(testDispatcher) { vm.uiState.collect() }

    vm.toggleServiceBookmark("PAN Card Services", "New PAN Card Apply", currentlySaved = false)
    assertTrue(vm.isBookmarked(BookmarkType.SERVICE, "PAN Card Services_New PAN Card Apply"))

    val saved = (vm.uiState.value.bookmarksState as CloudUiState.Success).data
    assertEquals(1, saved.size)

    vm.removeBookmark(saved.first().id)
    val afterRemove = (vm.uiState.value.bookmarksState as CloudUiState.Success).data
    assertTrue(afterRemove.isEmpty())

    job.cancel()
  }

  @Test
  fun prefillAndJumpToAlertsUpdateSelectedTab() = runTest {
    val vm = createViewModel()
    val job = launch(testDispatcher) { vm.uiState.collect() }

    vm.prefillInquiryFromService("New PAN Card Apply")
    assertEquals(AppTab.CONTACT, vm.uiState.value.selectedTab)
    assertEquals("New PAN Card Apply", vm.uiState.value.inquiryForm.selectedService)

    vm.jumpToAlerts(AlertCategory.SCHEME)
    assertEquals(AppTab.ALERTS, vm.uiState.value.selectedTab)
    assertEquals(AlertCategory.SCHEME, vm.uiState.value.selectedAlertCategory)

    vm.setAlertsEnabled(true)
    assertTrue(vm.uiState.value.alertsEnabled)

    job.cancel()
  }
}
