package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.security.MessageDigest
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.shopLocalDataStore: DataStore<Preferences> by
  preferencesDataStore(name = "rk_gallery_local_records")

interface ShopCloudStore {
  suspend fun ensureUserProfile(
    userId: String,
    displayName: String,
    email: String?,
    defaultAlertsEnabled: Boolean,
  ): Result<Unit>

  fun observeUserProfile(userId: String): Flow<UserProfile?>

  suspend fun updateAlertsPreference(userId: String, enabled: Boolean): Result<Unit>

  fun observeInquiries(userId: String): Flow<List<ServiceInquiry>>

  suspend fun submitInquiry(
    userId: String,
    customerName: String,
    phone: String,
    serviceName: String,
    message: String,
  ): Result<Unit>

  suspend fun markInquiryCompleted(userId: String, inquiryId: String): Result<Unit>

  suspend fun deleteInquiry(userId: String, inquiryId: String): Result<Unit>

  fun observeBookmarks(userId: String): Flow<List<SavedBookmark>>

  suspend fun toggleBookmark(
    userId: String,
    rawKey: String,
    itemTitle: String,
    categoryTitle: String,
    itemType: BookmarkType,
    currentlySaved: Boolean,
  ): Result<Unit>

  suspend fun removeBookmark(userId: String, bookmarkId: String): Result<Unit>
}

class LocalShopRepository(private val context: Context) : ShopCloudStore {

  private object Keys {
    val INQUIRIES_JSON = stringPreferencesKey("saved_inquiries_json")
    val BOOKMARKS_JSON = stringPreferencesKey("saved_bookmarks_json")
  }

  override suspend fun ensureUserProfile(
    userId: String,
    displayName: String,
    email: String?,
    defaultAlertsEnabled: Boolean,
  ): Result<Unit> = Result.success(Unit)

  override fun observeUserProfile(userId: String): Flow<UserProfile?> =
    context.shopLocalDataStore.data.map { null }

  override suspend fun updateAlertsPreference(userId: String, enabled: Boolean): Result<Unit> =
    Result.success(Unit)

  override fun observeInquiries(userId: String): Flow<List<ServiceInquiry>> =
    context.shopLocalDataStore.data.map { prefs ->
      parseInquiries(prefs[Keys.INQUIRIES_JSON], userId)
        .sortedByDescending { it.createdAtMillis }
    }

  override suspend fun submitInquiry(
    userId: String,
    customerName: String,
    phone: String,
    serviceName: String,
    message: String,
  ): Result<Unit> = runCatching {
    val cleanName = customerName.trim().take(80)
    val cleanPhone = phone.trim().take(20)
    val cleanService = serviceName.trim().take(150)
    val cleanMessage = message.trim().take(500)
    require(cleanName.length >= 2 && cleanPhone.length >= 10 && cleanService.isNotEmpty())

    val newItem =
      ServiceInquiry(
        id = UUID.randomUUID().toString(),
        userId = userId,
        customerName = cleanName,
        phone = cleanPhone,
        serviceName = cleanService,
        message = cleanMessage,
        status = InquiryStatus.OPEN.name,
        createdAtMillis = System.currentTimeMillis(),
      )

    context.shopLocalDataStore.edit { prefs ->
      val existing = parseInquiries(prefs[Keys.INQUIRIES_JSON], userId)
      prefs[Keys.INQUIRIES_JSON] = encodeInquiries(listOf(newItem) + existing)
    }
  }

  override suspend fun markInquiryCompleted(userId: String, inquiryId: String): Result<Unit> =
    runCatching {
      context.shopLocalDataStore.edit { prefs ->
        val updated =
          parseInquiries(prefs[Keys.INQUIRIES_JSON], userId).map {
            if (it.id == inquiryId) it.copy(status = InquiryStatus.COMPLETED.name) else it
          }
        prefs[Keys.INQUIRIES_JSON] = encodeInquiries(updated)
      }
    }

  override suspend fun deleteInquiry(userId: String, inquiryId: String): Result<Unit> =
    runCatching {
      context.shopLocalDataStore.edit { prefs ->
        val filtered =
          parseInquiries(prefs[Keys.INQUIRIES_JSON], userId).filterNot { it.id == inquiryId }
        prefs[Keys.INQUIRIES_JSON] = encodeInquiries(filtered)
      }
    }

  override fun observeBookmarks(userId: String): Flow<List<SavedBookmark>> =
    context.shopLocalDataStore.data.map { prefs ->
      parseBookmarks(prefs[Keys.BOOKMARKS_JSON], userId)
        .sortedByDescending { it.createdAtMillis }
    }

  override suspend fun toggleBookmark(
    userId: String,
    rawKey: String,
    itemTitle: String,
    categoryTitle: String,
    itemType: BookmarkType,
    currentlySaved: Boolean,
  ): Result<Unit> = runCatching {
    val docId = sanitizeBookmarkId(itemType, rawKey)
    context.shopLocalDataStore.edit { prefs ->
      val current = parseBookmarks(prefs[Keys.BOOKMARKS_JSON], userId)
      val next =
        if (currentlySaved) {
          current.filterNot { it.id == docId }
        } else {
          val bookmark =
            SavedBookmark(
              id = docId,
              userId = userId,
              itemTitle = itemTitle.trim().take(180),
              categoryTitle = categoryTitle.trim().take(150),
              itemType = itemType.name,
              createdAtMillis = System.currentTimeMillis(),
            )
          listOf(bookmark) + current.filterNot { it.id == docId }
        }
      prefs[Keys.BOOKMARKS_JSON] = encodeBookmarks(next)
    }
  }

  override suspend fun removeBookmark(userId: String, bookmarkId: String): Result<Unit> =
    runCatching {
      context.shopLocalDataStore.edit { prefs ->
        val next = parseBookmarks(prefs[Keys.BOOKMARKS_JSON], userId).filterNot { it.id == bookmarkId }
        prefs[Keys.BOOKMARKS_JSON] = encodeBookmarks(next)
      }
    }

  private fun parseInquiries(raw: String?, userId: String): List<ServiceInquiry> {
    if (raw.isNullOrBlank()) return emptyList()
    return runCatching {
        val arr = JSONArray(raw)
        buildList {
          for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            add(
              ServiceInquiry(
                id = obj.optString("id"),
                userId = obj.optString("userId", userId),
                customerName = obj.optString("customerName"),
                phone = obj.optString("phone"),
                serviceName = obj.optString("serviceName"),
                message = obj.optString("message"),
                status = obj.optString("status", InquiryStatus.OPEN.name),
                createdAtMillis = obj.optLong("createdAtMillis", 0L),
              )
            )
          }
        }
      }
      .getOrDefault(emptyList())
  }

  private fun encodeInquiries(items: List<ServiceInquiry>): String {
    val arr = JSONArray()
    items.forEach { item ->
      arr.put(
        JSONObject()
          .put("id", item.id)
          .put("userId", item.userId)
          .put("customerName", item.customerName)
          .put("phone", item.phone)
          .put("serviceName", item.serviceName)
          .put("message", item.message)
          .put("status", item.status)
          .put("createdAtMillis", item.createdAtMillis)
      )
    }
    return arr.toString()
  }

  private fun parseBookmarks(raw: String?, userId: String): List<SavedBookmark> {
    if (raw.isNullOrBlank()) return emptyList()
    return runCatching {
        val arr = JSONArray(raw)
        buildList {
          for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            add(
              SavedBookmark(
                id = obj.optString("id"),
                userId = obj.optString("userId", userId),
                itemTitle = obj.optString("itemTitle"),
                categoryTitle = obj.optString("categoryTitle"),
                itemType = obj.optString("itemType", BookmarkType.SERVICE.name),
                createdAtMillis = obj.optLong("createdAtMillis", 0L),
              )
            )
          }
        }
      }
      .getOrDefault(emptyList())
  }

  private fun encodeBookmarks(items: List<SavedBookmark>): String {
    val arr = JSONArray()
    items.forEach { item ->
      arr.put(
        JSONObject()
          .put("id", item.id)
          .put("userId", item.userId)
          .put("itemTitle", item.itemTitle)
          .put("categoryTitle", item.categoryTitle)
          .put("itemType", item.itemType)
          .put("createdAtMillis", item.createdAtMillis)
      )
    }
    return arr.toString()
  }

  companion object {
    fun sanitizeBookmarkId(type: BookmarkType, rawKey: String): String {
      val prefix = if (type == BookmarkType.ALERT) "alt_" else "svc_"
      val digest = MessageDigest.getInstance("SHA-256").digest(rawKey.toByteArray())
      val hash8 = digest.take(4).joinToString("") { "%02x".format(it) }
      val ascii =
        rawKey
          .replace(Regex("[^a-zA-Z0-9_-]+"), "_")
          .trim('_')
          .take(80)
      return if (ascii.isNotEmpty()) "${prefix}${ascii}_$hash8" else "${prefix}$hash8"
    }
  }
}
