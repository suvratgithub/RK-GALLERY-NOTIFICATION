package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.AlertCategory
import com.example.data.BookmarkType
import com.example.data.JobAlertItem
import com.example.data.LocalShopRepository
import com.example.ui.CloudUiState
import com.example.ui.ShopUiState
import com.example.ui.components.SectionHeader
import com.example.ui.theme.ShopTheme

@Composable
fun AlertsScreen(
  uiState: ShopUiState,
  onSelectFilter: (AlertCategory) -> Unit,
  onToggleAlerts: (Boolean) -> Unit,
  onApplyWhatsApp: (String) -> Unit,
  onJoinWhatsAppGroup: () -> Unit,
  onCallShop: () -> Unit,
  onToggleAlertBookmark: (JobAlertItem, Boolean) -> Unit,
  modifier: Modifier = Modifier,
) {
  val shopColors = ShopTheme.colors

  LazyColumn(
    modifier = modifier.fillMaxSize().testTag("alerts_screen_list"),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    // 1. Header
    item {
      SectionHeader(
        tagText = "Daily Updates & Alerts",
        titlePrefix = "Govt Jobs, Admit Cards ",
        titleHighlight = "& Yojana Alerts",
        subtitle =
          "Stay updated with Madhya Pradesh and Central Govt recruitments, exam admit cards, results, and MP schemes. Tap Apply to send details on WhatsApp!",
        tagColor = shopColors.amber,
        tagBackground = shopColors.amberContainer,
        modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp),
      )
    }

    // 2. Notification Opt-in Banner
    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, shopColors.amber.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp),
      ) {
        Column(
          modifier =
            Modifier.fillMaxWidth()
              .background(
                Brush.linearGradient(
                  listOf(shopColors.amberContainer, shopColors.amberContainer.copy(alpha = 0.55f))
                )
              )
              .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
          ) {
            Box(
              modifier =
                Modifier.size(44.dp)
                  .clip(CircleShape)
                  .background(
                    if (uiState.alertsEnabled) shopColors.emerald else shopColors.amber
                  ),
              contentAlignment = Alignment.Center,
            ) {
              Icon(
                imageVector =
                  if (uiState.alertsEnabled) Icons.Filled.CheckCircle
                  else Icons.Filled.NotificationsActive,
                contentDescription = null,
                tint = Color.White,
              )
            }
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text =
                  if (uiState.alertsEnabled) "Mobile Alerts Active"
                  else "Get Daily Job & Scheme Alerts",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
              )
              Text(
                text =
                  if (uiState.alertsEnabled)
                    "Notifications enabled on this device for new forms & yojana updates."
                  else "Turn on mobile alerts or join our free WhatsApp update group.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
            Switch(
              checked = uiState.alertsEnabled,
              onCheckedChange = onToggleAlerts,
              modifier = Modifier.testTag("alerts_enable_switch"),
            )
          }

          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (!uiState.alertsEnabled) {
              Button(
                onClick = { onToggleAlerts(true) },
                colors = ButtonDefaults.buttonColors(containerColor = shopColors.amber),
                shape = RoundedCornerShape(50),
                modifier = Modifier.height(48.dp).testTag("alerts_enable_button"),
              ) {
                Icon(
                  Icons.Filled.NotificationsActive,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Enable Alerts")
              }
            }
            Button(
              onClick = onJoinWhatsAppGroup,
              colors = ButtonDefaults.buttonColors(containerColor = shopColors.whatsappDark),
              shape = RoundedCornerShape(50),
              modifier = Modifier.height(48.dp).testTag("alerts_wagroup_button"),
            ) {
              Icon(Icons.Filled.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("WhatsApp Alert Group")
            }
          }
        }
      }
    }

    // 3. Category Filter Chips
    item {
      LazyRow(
        modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        items(AlertCategory.entries) { cat ->
          val selected = uiState.selectedAlertCategory == cat
          FilterChip(
            selected = selected,
            onClick = { onSelectFilter(cat) },
            label = { Text(cat.label) },
            colors =
              FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = Color.White,
              ),
            shape = RoundedCornerShape(50),
            modifier = Modifier.height(40.dp).testTag("alert_filter_${cat.name}"),
          )
        }
      }
    }

    // 4. Alert Cards
    val savedBookmarkIds =
      (uiState.bookmarksState as? CloudUiState.Success)?.data?.map { it.id }?.toSet().orEmpty()
    items(uiState.filteredAlerts, key = { it.id }) { alert ->
      val bookmarkId = LocalShopRepository.sanitizeBookmarkId(BookmarkType.ALERT, alert.id)
      val isSaved = bookmarkId in savedBookmarkIds
      JobAlertCard(
        alert = alert,
        isBookmarked = isSaved,
        onToggleBookmark = { onToggleAlertBookmark(alert, isSaved) },
        onApplyWhatsApp = { onApplyWhatsApp(alert.whatsappPrefill) },
        onCallShop = onCallShop,
        modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp),
      )
    }

    item { Spacer(modifier = Modifier.height(12.dp)) }
  }
}

@Composable
private fun JobAlertCard(
  alert: JobAlertItem,
  isBookmarked: Boolean,
  onToggleBookmark: () -> Unit,
  onApplyWhatsApp: () -> Unit,
  onCallShop: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val shopColors = ShopTheme.colors
  val (badgeBg, badgeFg) =
    when (alert.category) {
      AlertCategory.NEW_JOB -> shopColors.emeraldContainer to shopColors.emerald
      AlertCategory.ADMIT_CARD ->
        MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.primary
      AlertCategory.RESULT -> Color(0xFFF3E5F5) to Color(0xFF7B1FA2)
      AlertCategory.SCHEME -> shopColors.amberContainer to shopColors.amber
      AlertCategory.ALL ->
        MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.primary
    }

  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)),
    modifier = modifier.testTag("job_alert_card_${alert.id}"),
  ) {
    Column(
      modifier = Modifier.fillMaxWidth().padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Surface(color = badgeBg, shape = RoundedCornerShape(50)) {
          Text(
            text = alert.badgeLabel.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = badgeFg,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
          )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = alert.urgencyLabel,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = shopColors.liveRed,
          )
          IconButton(
            onClick = onToggleBookmark,
            modifier =
              Modifier.minimumInteractiveComponentSize()
                .testTag("alert_bookmark_${alert.id}"),
          ) {
            Icon(
              imageVector =
                if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
              contentDescription = if (isBookmarked) "Remove alert bookmark" else "Save alert",
              tint =
                if (isBookmarked) MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
      }

      Text(
        text = alert.title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
      )

      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
          text = "${alert.line1Label}: ${alert.line1Value}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
          text = "${alert.line2Label}: ${alert.line2Value}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
          text = alert.note,
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurface,
        )
      }

      HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        Button(
          onClick = onApplyWhatsApp,
          colors = ButtonDefaults.buttonColors(containerColor = shopColors.whatsappDark),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.weight(1f).height(48.dp).testTag("alert_whatsapp_${alert.id}"),
        ) {
          Icon(Icons.Filled.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Apply / Inquire")
        }
        OutlinedButton(
          onClick = onCallShop,
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.height(48.dp).testTag("alert_call_${alert.id}"),
        ) {
          Icon(Icons.Filled.Call, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Call")
        }
      }
    }
  }
}
