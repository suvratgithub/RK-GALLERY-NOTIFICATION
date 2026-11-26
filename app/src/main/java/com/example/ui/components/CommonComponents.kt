package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.data.SelectedServiceContext
import com.example.data.ServiceIconKey
import com.example.data.StationeryIconKey
import com.example.data.TickerHeadline
import com.example.ui.theme.ShopTheme

@Composable
fun ShopTopHeader(
  alertsEnabled: Boolean,
  onAlertsClick: () -> Unit,
  onInstagramClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val shopColors = ShopTheme.colors
  Surface(
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
    shadowElevation = 2.dp,
    modifier = modifier.fillMaxWidth(),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f),
      ) {
        Box(
          modifier =
            Modifier.size(40.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(
                Brush.linearGradient(
                  listOf(MaterialTheme.colorScheme.primary, shopColors.cyberCyan)
                )
              ),
          contentAlignment = Alignment.Center,
        ) {
          Text(
            text = "RK",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "R K Gallery & Stationers",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
          Text(
            text = "Cyber Cafe • Jamtara, MP • 9 AM–9 PM",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        }
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(
          onClick = onInstagramClick,
          modifier = Modifier.minimumInteractiveComponentSize().testTag("header_instagram_button"),
        ) {
          Box(
            modifier =
              Modifier.size(32.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(
                  Brush.linearGradient(
                    listOf(
                      shopColors.instagramStart,
                      shopColors.instagramMid,
                      shopColors.instagramEnd,
                    )
                  )
                ),
            contentAlignment = Alignment.Center,
          ) {
            Icon(
              imageVector = Icons.Filled.CameraAlt,
              contentDescription = "Open R K Gallery Instagram",
              tint = Color.White,
              modifier = Modifier.size(17.dp),
            )
          }
        }
        IconButton(
          onClick = onAlertsClick,
          modifier = Modifier.minimumInteractiveComponentSize().testTag("header_alerts_button"),
        ) {
          Box(contentAlignment = Alignment.TopEnd) {
            Icon(
              imageVector = Icons.Filled.NotificationsActive,
              contentDescription = "Open Job and Yojana Alerts",
              tint =
                if (alertsEnabled) shopColors.emerald else MaterialTheme.colorScheme.secondary,
            )
            if (!alertsEnabled) {
              Box(
                modifier =
                  Modifier.size(8.dp).clip(CircleShape).background(shopColors.liveRed)
              )
            }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LiveUpdatesTicker(
  headlines: List<TickerHeadline>,
  onTickerClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val shopColors = ShopTheme.colors
  val tickerText = buildAnnotatedString {
    headlines.forEachIndexed { idx, item ->
      withStyle(SpanStyle(color = Color(0xFFFFCA28), fontWeight = FontWeight.Bold)) {
        append("${item.prefix} ")
      }
      withStyle(SpanStyle(color = Color.White)) { append(item.headline) }
      if (idx < headlines.lastIndex) {
        append("     •     ")
      }
    }
  }

  Row(
    modifier =
      modifier
        .fillMaxWidth()
        .background(shopColors.tickerBackground)
        .clickable(onClick = onTickerClick)
        .padding(horizontal = 12.dp, vertical = 8.dp)
        .testTag("live_updates_ticker"),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Surface(
      color = shopColors.liveRed,
      shape = RoundedCornerShape(6.dp),
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
      ) {
        Icon(
          imageVector = Icons.Filled.Bolt,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier.size(12.dp),
        )
        Text(
          text = "LIVE",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.ExtraBold,
          color = Color.White,
        )
      }
    }
    Spacer(modifier = Modifier.width(10.dp))
    Text(
      text = tickerText,
      style = MaterialTheme.typography.labelMedium,
      maxLines = 1,
      modifier = Modifier.weight(1f).basicMarquee(iterations = Int.MAX_VALUE),
    )
  }
}

@Composable
fun SectionHeader(
  tagText: String,
  titlePrefix: String,
  titleHighlight: String,
  subtitle: String,
  tagColor: Color = MaterialTheme.colorScheme.primary,
  tagBackground: Color = MaterialTheme.colorScheme.primaryContainer,
  highlightColor: Color = MaterialTheme.colorScheme.secondary,
  modifier: Modifier = Modifier,
) {
  Column(modifier = modifier.fillMaxWidth()) {
    Surface(
      color = tagBackground,
      shape = RoundedCornerShape(50),
    ) {
      Text(
        text = tagText.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = tagColor,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
      )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text =
        buildAnnotatedString {
          append(titlePrefix)
          withStyle(SpanStyle(color = highlightColor)) { append(titleHighlight) }
        },
      style = MaterialTheme.typography.headlineSmall,
      color = MaterialTheme.colorScheme.primary,
    )
    if (subtitle.isNotBlank()) {
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceTagActionSheet(
  context: SelectedServiceContext,
  isBookmarked: Boolean,
  onToggleBookmark: () -> Unit,
  onDismiss: () -> Unit,
  onWhatsAppClick: () -> Unit,
  onPrefillContactClick: () -> Unit,
  onCallShopClick: () -> Unit,
) {
  val shopColors = ShopTheme.colors
  ModalBottomSheet(
    onDismissRequest = onDismiss,
    containerColor = MaterialTheme.colorScheme.surface,
  ) {
    Column(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 28.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Surface(
          color = MaterialTheme.colorScheme.primaryContainer,
          shape = RoundedCornerShape(50),
        ) {
          Text(
            text = context.categoryTitle,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
          )
        }
        IconButton(
          onClick = onToggleBookmark,
          modifier = Modifier.minimumInteractiveComponentSize().testTag("sheet_bookmark_button"),
        ) {
          Icon(
            imageVector =
              if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
            contentDescription = if (isBookmarked) "Remove bookmark" else "Save service bookmark",
            tint =
              if (isBookmarked) MaterialTheme.colorScheme.secondary
              else MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
      Text(
        text = context.serviceName,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface,
      )
      Text(
        text =
          "Visit R K Gallery & Stationers (9 AM – 9 PM daily) or contact us directly for required documents, fees, and quick processing.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
      Spacer(modifier = Modifier.height(4.dp))
      Button(
        onClick = onWhatsAppClick,
        colors = ButtonDefaults.buttonColors(containerColor = shopColors.whatsappDark),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth().height(50.dp).testTag("sheet_whatsapp_button"),
      ) {
        Icon(Icons.Filled.Chat, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Inquire on WhatsApp")
      }
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        OutlinedButton(
          onClick = onPrefillContactClick,
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.weight(1f).height(48.dp).testTag("sheet_prefill_button"),
        ) {
          Text("Inquiry Form", maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        OutlinedButton(
          onClick = onCallShopClick,
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.weight(1f).height(48.dp).testTag("sheet_call_button"),
        ) {
          Icon(
            Icons.Filled.Call,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(18.dp),
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            "Call Shop",
            color = MaterialTheme.colorScheme.secondary,
            maxLines = 1,
          )
        }
      }
    }
  }
}

fun mapServiceIcon(key: ServiceIconKey): ImageVector =
  when (key) {
    ServiceIconKey.DOCUMENTS -> Icons.Filled.Description
    ServiceIconKey.AADHAAR -> Icons.Filled.CreditCard
    ServiceIconKey.PAN -> Icons.Filled.Badge
    ServiceIconKey.VOTER -> Icons.Filled.HowToVote
    ServiceIconKey.JOBS -> Icons.Filled.Work
    ServiceIconKey.EXAMS -> Icons.Filled.Quiz
    ServiceIconKey.MP_ONLINE -> Icons.Filled.AccountBalance
    ServiceIconKey.SAMAGRA -> Icons.Filled.Groups
    ServiceIconKey.LABOUR -> Icons.Filled.Engineering
    ServiceIconKey.RAILWAY -> Icons.Filled.Train
    ServiceIconKey.DRIVING -> Icons.Filled.DirectionsCar
    ServiceIconKey.EDUCATION -> Icons.Filled.School
    ServiceIconKey.PRINTING -> Icons.Filled.Print
    ServiceIconKey.PDF_EDIT -> Icons.Filled.PictureAsPdf
    ServiceIconKey.GRAPHIC_DESIGN -> Icons.Filled.Palette
    ServiceIconKey.COMPUTER -> Icons.Filled.Keyboard
    ServiceIconKey.PASSPORT -> Icons.Filled.FlightTakeoff
    ServiceIconKey.INSURANCE -> Icons.Filled.HealthAndSafety
    ServiceIconKey.PUBLIC_SERVICES -> Icons.Filled.Public
    ServiceIconKey.SHOP_PRODUCTS -> Icons.Filled.Storefront
  }

fun mapStationeryIcon(key: StationeryIconKey): ImageVector =
  when (key) {
    StationeryIconKey.PEN -> Icons.Filled.Edit
    StationeryIconKey.ART -> Icons.Filled.Palette
    StationeryIconKey.BOOKS -> Icons.AutoMirrored.Filled.LibraryBooks
    StationeryIconKey.PRINT -> Icons.Filled.Print
    StationeryIconKey.FRAMES -> Icons.Filled.PhotoLibrary
    StationeryIconKey.OFFICE -> Icons.Filled.FolderOpen
  }
