package com.example.data

enum class AppTab(val route: String, val label: String) {
  HOME("home", "Home"),
  SERVICES("services", "Services"),
  ALERTS("alerts", "Alerts"),
  CONTACT("contact", "Contact"),
}

enum class ServiceIconKey {
  DOCUMENTS,
  AADHAAR,
  PAN,
  VOTER,
  JOBS,
  EXAMS,
  MP_ONLINE,
  SAMAGRA,
  LABOUR,
  RAILWAY,
  DRIVING,
  EDUCATION,
  PRINTING,
  PDF_EDIT,
  GRAPHIC_DESIGN,
  COMPUTER,
  PASSPORT,
  INSURANCE,
  PUBLIC_SERVICES,
  SHOP_PRODUCTS,
}

data class ServiceCategory(
  val id: Int,
  val hindiTitle: String,
  val englishTitle: String,
  val iconKey: ServiceIconKey,
  val services: List<String>,
)

enum class StationeryIconKey {
  PEN,
  ART,
  BOOKS,
  PRINT,
  FRAMES,
  OFFICE,
}

data class StationeryCategory(
  val id: String,
  val title: String,
  val description: String,
  val iconKey: StationeryIconKey,
)

data class GalleryHighlight(
  val id: String,
  val title: String,
  val subtitle: String,
  val iconKey: StationeryIconKey,
  val paletteIndex: Int,
)

data class CustomerReview(
  val id: String,
  val author: String,
  val location: String,
  val initial: String,
  val rating: Int,
  val quote: String,
)

enum class AlertCategory(val label: String) {
  ALL("All Updates"),
  NEW_JOB("New Jobs"),
  ADMIT_CARD("Admit Card"),
  RESULT("Result"),
  SCHEME("Govt Schemes"),
}

data class JobAlertItem(
  val id: String,
  val category: AlertCategory,
  val badgeLabel: String,
  val urgencyLabel: String,
  val title: String,
  val line1Label: String,
  val line1Value: String,
  val line2Label: String,
  val line2Value: String,
  val note: String,
  val whatsappPrefill: String,
)

data class TickerHeadline(
  val id: String,
  val prefix: String,
  val headline: String,
  val targetCategory: AlertCategory,
)

data class SelectedServiceContext(
  val categoryTitle: String,
  val serviceName: String,
)

data class InquiryFormState(
  val name: String = "",
  val phone: String = "",
  val selectedService: String = "Aadhaar Card Work",
  val message: String = "",
  val nameError: String? = null,
  val phoneError: String? = null,
  val isSubmitted: Boolean = false,
)
