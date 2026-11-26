package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.UnfoldLess
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.ServiceCategory
import com.example.ui.ShopUiState
import com.example.ui.components.SectionHeader
import com.example.ui.components.mapServiceIcon
import com.example.ui.theme.ShopTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ServicesScreen(
  uiState: ShopUiState,
  onSearchChange: (String) -> Unit,
  onToggleCategory: (Int) -> Unit,
  onExpandAllToggle: (Boolean) -> Unit,
  onServiceTagClick: (categoryTitle: String, serviceName: String) -> Unit,
  onCallShop: () -> Unit,
  onWhatsAppGroup: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val shopColors = ShopTheme.colors
  val allExpanded = uiState.expandedCategoryIds.size == uiState.allCategories.size

  LazyColumn(
    modifier = modifier.fillMaxSize().testTag("services_screen_list"),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    item {
      Column(
        modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        SectionHeader(
          tagText = "Cyber Cafe & Online Services",
          titlePrefix = "All ",
          titleHighlight = "300+ Services",
          subtitle =
            "Tap any category below to browse available services, or search in English or Hindi. Tap any service tag to inquire immediately!",
          tagColor = shopColors.cyberCyan,
          tagBackground = shopColors.cyberCyanContainer,
          highlightColor = shopColors.cyberCyan,
        )

        // Summary Count Strip
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          modifier = Modifier.fillMaxWidth(),
        ) {
          FlowRow(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            SummaryBadge("20 Categories", "All Listed", shopColors.amber)
            SummaryBadge("300+ Services", "Cyber & Govt", shopColors.cyberCyan)
            SummaryBadge("9 AM–9 PM", "Daily Open", shopColors.emerald)
          }
        }

        // Search Input
        OutlinedTextField(
          value = uiState.serviceSearchQuery,
          onValueChange = onSearchChange,
          placeholder = {
            Text("Search Aadhaar, PAN, MP Police, Samagra, आय, जाति...")
          },
          leadingIcon = {
            Icon(
              Icons.Filled.Search,
              contentDescription = "Search services",
              tint = MaterialTheme.colorScheme.primary,
            )
          },
          trailingIcon = {
            if (uiState.serviceSearchQuery.isNotEmpty()) {
              IconButton(
                onClick = { onSearchChange("") },
                modifier = Modifier.minimumInteractiveComponentSize().testTag("clear_search_button"),
              ) {
                Icon(Icons.Filled.Close, contentDescription = "Clear search")
              }
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          colors =
            OutlinedTextFieldDefaults.colors(
              focusedContainerColor = MaterialTheme.colorScheme.surface,
              unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
          modifier = Modifier.fillMaxWidth().testTag("services_search_input"),
        )

        if (uiState.serviceSearchQuery.isBlank()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = "${uiState.filteredCategories.size} Service Categories",
              style = MaterialTheme.typography.labelLarge,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            TextButton(
              onClick = { onExpandAllToggle(!allExpanded) },
              modifier = Modifier.minimumInteractiveComponentSize().testTag("expand_all_button"),
            ) {
              Icon(
                imageVector = if (allExpanded) Icons.Filled.UnfoldLess else Icons.Filled.UnfoldMore,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(if (allExpanded) "Collapse All" else "Expand All")
            }
          }
        }
      }
    }

    if (uiState.filteredCategories.isEmpty()) {
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp),
        ) {
          Column(
            modifier = Modifier.fillMaxWidth().padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            Icon(
              Icons.Filled.SearchOff,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(44.dp),
            )
            Text(
              text = "No matching services found",
              style = MaterialTheme.typography.titleMedium,
            )
            Text(
              text =
                "Try another keyword or clear your search to browse all 20 service categories.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedButton(
              onClick = { onSearchChange("") },
              modifier = Modifier.height(48.dp),
            ) {
              Text("Show All Categories")
            }
          }
        }
      }
    } else {
      items(uiState.filteredCategories, key = { it.id }) { category ->
        val isExpanded = category.id in uiState.expandedCategoryIds
        ServiceCategoryAccordion(
          category = category,
          isExpanded = isExpanded,
          onToggle = { onToggleCategory(category.id) },
          onTagClick = { serviceName ->
            onServiceTagClick(category.englishTitle, serviceName)
          },
          modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp),
        )
      }
    }

    // Bottom Cyber Cafe CTA Card
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp).padding(top = 6.dp),
      ) {
        Column(
          modifier =
            Modifier.fillMaxWidth()
              .background(
                Brush.linearGradient(listOf(shopColors.cyberCyan, Color(0xFF004D40)))
              )
              .padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          Text(
            text = "Need any online work done today?",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
          )
          Text(
            text =
              "Walk straight in or call us — our experienced staff handles every form accurately. No appointment needed!",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.88f),
          )
          Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
              onClick = onCallShop,
              colors =
                ButtonDefaults.buttonColors(
                  containerColor = Color.White,
                  contentColor = shopColors.cyberCyan,
                ),
              shape = RoundedCornerShape(50),
              modifier = Modifier.height(48.dp).testTag("services_cta_call_button"),
            ) {
              Icon(Icons.Filled.Call, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("091798 18444")
            }
            Button(
              onClick = onWhatsAppGroup,
              colors = ButtonDefaults.buttonColors(containerColor = shopColors.whatsappDark),
              shape = RoundedCornerShape(50),
              modifier = Modifier.height(48.dp).testTag("services_cta_wagroup_button"),
            ) {
              Icon(Icons.Filled.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("WA Group")
            }
          }
        }
      }
    }

    item { Spacer(modifier = Modifier.height(12.dp)) }
  }
}

@Composable
private fun SummaryBadge(label: String, pill: String, accentColor: Color) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(6.dp),
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.labelLarge,
      color = MaterialTheme.colorScheme.primary,
    )
    Surface(color = accentColor, shape = RoundedCornerShape(50)) {
      Text(
        text = pill,
        style = MaterialTheme.typography.labelSmall,
        color = Color.White,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
      )
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ServiceCategoryAccordion(
  category: ServiceCategory,
  isExpanded: Boolean,
  onToggle: () -> Unit,
  onTagClick: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val rotation by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f, label = "arrow")

  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = if (isExpanded) 4.dp else 2.dp),
    modifier = modifier.testTag("service_category_${category.id}"),
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      val headerModifier =
        if (isExpanded) {
          Modifier.fillMaxWidth()
            .background(
              Brush.linearGradient(
                listOf(MaterialTheme.colorScheme.primary, Color(0xFF3949AB))
              )
            )
        } else {
          Modifier.fillMaxWidth()
        }

      Row(
        modifier =
          headerModifier
            .clickable(onClick = onToggle)
            .padding(horizontal = 14.dp, vertical = 14.dp)
            .testTag("service_category_header_${category.id}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        Box(
          modifier =
            Modifier.size(42.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(
                if (isExpanded) Color.White.copy(alpha = 0.2f)
                else MaterialTheme.colorScheme.primaryContainer
              ),
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            imageVector = mapServiceIcon(category.iconKey),
            contentDescription = null,
            tint = if (isExpanded) Color.White else MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp),
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "${category.id}. ${category.hindiTitle}",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = if (isExpanded) Color.White else MaterialTheme.colorScheme.primary,
          )
          Text(
            text = category.englishTitle,
            style = MaterialTheme.typography.bodySmall,
            color =
              if (isExpanded) Color.White.copy(alpha = 0.85f)
              else MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }

        Surface(
          color =
            if (isExpanded) Color.White.copy(alpha = 0.2f)
            else MaterialTheme.colorScheme.secondaryContainer,
          shape = RoundedCornerShape(50),
        ) {
          Text(
            text = "${category.services.size} Services",
            style = MaterialTheme.typography.labelSmall,
            color = if (isExpanded) Color.White else MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
          )
        }

        Icon(
          imageVector = Icons.Filled.KeyboardArrowDown,
          contentDescription = if (isExpanded) "Collapse category" else "Expand category",
          tint =
            if (isExpanded) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.rotate(rotation),
        )
      }

      AnimatedVisibility(
        visible = isExpanded,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut(),
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
          FlowRow(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            category.services.forEach { serviceName ->
              Surface(
                onClick = { onTagClick(serviceName) },
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.background,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.minimumInteractiveComponentSize(),
              ) {
                Text(
                  text = serviceName,
                  style = MaterialTheme.typography.labelMedium,
                  color = MaterialTheme.colorScheme.onSurface,
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                )
              }
            }
          }
        }
      }
    }
  }
}
