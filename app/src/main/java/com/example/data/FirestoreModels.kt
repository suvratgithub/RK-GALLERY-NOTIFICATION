package com.example.data

enum class InquiryStatus(val displayLabel: String) {
  OPEN("Open"),
  CONTACTED("Contacted"),
  COMPLETED("Completed");

  companion object {
    fun fromRaw(raw: String): InquiryStatus =
      entries.firstOrNull { it.name == raw } ?: OPEN
  }
}

enum class BookmarkType(val displayLabel: String) {
  SERVICE("Online Service"),
  ALERT("Job / Yojana Alert");

  companion object {
    fun fromRaw(raw: String): BookmarkType =
      entries.firstOrNull { it.name == raw } ?: SERVICE
  }
}

data class UserProfile(
  val id: String = "",
  val userId: String = "",
  val displayName: String = "",
  val email: String? = null,
  val alertsEnabled: Boolean = false,
)

data class ServiceInquiry(
  val id: String = "",
  val userId: String = "",
  val customerName: String = "",
  val phone: String = "",
  val serviceName: String = "",
  val message: String = "",
  val status: String = InquiryStatus.OPEN.name,
  val createdAtMillis: Long = 0L,
)

data class SavedBookmark(
  val id: String = "",
  val userId: String = "",
  val itemTitle: String = "",
  val categoryTitle: String = "",
  val itemType: String = BookmarkType.SERVICE.name,
  val createdAtMillis: Long = 0L,
)
