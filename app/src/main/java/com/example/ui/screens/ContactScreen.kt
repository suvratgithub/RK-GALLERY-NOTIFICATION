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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.InquiryStatus
import com.example.data.ServiceInquiry
import com.example.data.ShopCatalogRepository
import com.example.ui.CloudUiState
import com.example.ui.ShopUiState
import com.example.ui.components.SectionHeader
import com.example.ui.theme.ShopTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
  uiState: ShopUiState,
  onCallShop: () -> Unit,
  onWhatsAppDirect: () -> Unit,
  onWhatsAppGroup: () -> Unit,
  onOpenInstagram: () -> Unit,
  onOpenMaps: () -> Unit,
  onNameChange: (String) -> Unit,
  onPhoneChange: (String) -> Unit,
  onServiceSelected: (String) -> Unit,
  onMessageChange: (String) -> Unit,
  onSubmitInquiry: () -> Unit,
  onResetConfirmation: () -> Unit,
  onMarkInquiryCompleted: (String) -> Unit,
  onDeleteInquiry: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val shopColors = ShopTheme.colors
  val form = uiState.inquiryForm
  var serviceDropdownExpanded by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = modifier.fillMaxSize().testTag("contact_screen_list"),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    // 1. Header
    item {
      SectionHeader(
        tagText = "Visit Or Reach Us",
        titlePrefix = "Contact ",
        titleHighlight = "R K Gallery",
        subtitle =
          "Visit our shop, call us, or connect on WhatsApp & Instagram — open daily 9:00 AM to 9:00 PM!",
        modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp),
      )
    }

    // 2. Social & Direct Action Cards (Call, WhatsApp, WA Group, Instagram)
    item {
      Column(
        modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        // Instagram Link Card
        Card(
          onClick = onOpenInstagram,
          shape = RoundedCornerShape(16.dp),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          modifier = Modifier.fillMaxWidth().testTag("contact_instagram_card"),
        ) {
          Row(
            modifier =
              Modifier.fillMaxWidth()
                .background(
                  Brush.linearGradient(
                    listOf(
                      shopColors.instagramStart,
                      shopColors.instagramMid,
                      shopColors.instagramEnd,
                    )
                  )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
          ) {
            Box(
              modifier =
                Modifier.size(44.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .background(Color.White.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center,
            ) {
              Icon(Icons.Filled.CameraAlt, contentDescription = "Instagram", tint = Color.White)
            }
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Instagram",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.85f),
              )
              Text(
                text = ShopCatalogRepository.INSTAGRAM_HANDLE,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
              )
            }
            Surface(color = Color.White, shape = RoundedCornerShape(50)) {
              Text(
                text = "Open Profile",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = shopColors.instagramMid,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
              )
            }
          }
        }

        // Direct Call & WhatsApp Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          Button(
            onClick = onCallShop,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.weight(1f).height(52.dp).testTag("contact_call_button"),
          ) {
            Icon(Icons.Filled.Call, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(ShopCatalogRepository.PHONE_DISPLAY)
          }
          Button(
            onClick = onWhatsAppDirect,
            colors = ButtonDefaults.buttonColors(containerColor = shopColors.whatsappDark),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.weight(1f).height(52.dp).testTag("contact_whatsapp_button"),
          ) {
            Icon(Icons.Filled.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("WhatsApp")
          }
        }

        OutlinedButton(
          onClick = onWhatsAppGroup,
          border = BorderStroke(1.dp, shopColors.emerald),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth().height(48.dp).testTag("contact_wagroup_button"),
        ) {
          Icon(
            Icons.Filled.Groups,
            contentDescription = null,
            tint = shopColors.emerald,
            modifier = Modifier.size(18.dp),
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text("Join WhatsApp Community Group", color = shopColors.emerald)
        }
      }
    }

    // 3. Store Details + Google Maps
    item {
      Column(
        modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        ContactInfoItem(
          icon = Icons.Filled.LocationOn,
          label = "Store Address",
          value = ShopCatalogRepository.ADDRESS,
        )
        ContactInfoItem(
          icon = Icons.Filled.Person,
          label = "Proprietor / Shop Owner",
          value = ShopCatalogRepository.OWNER_NAME,
        )
        ContactInfoItem(
          icon = Icons.Filled.Schedule,
          label = "Working Hours",
          value = ShopCatalogRepository.WORKING_HOURS,
        )
        ContactInfoItem(
          icon = Icons.Filled.Laptop,
          label = "Cyber Cafe & Online Services",
          value = "20 Categories • 300+ Services • Walk-in anytime!",
          iconBg = shopColors.cyberCyan,
        )

        Card(
          shape = RoundedCornerShape(18.dp),
          modifier = Modifier.fillMaxWidth(),
        ) {
          Row(
            modifier =
              Modifier.fillMaxWidth()
                .background(
                  Brush.linearGradient(
                    listOf(MaterialTheme.colorScheme.primary, Color(0xFF3949AB))
                  )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            Column(modifier = Modifier.weight(1f).padding(end = 10.dp)) {
              Text(
                text = "Find Us on Google Maps",
                style = MaterialTheme.typography.titleSmall,
                color = Color.White,
              )
              Text(
                text = ShopCatalogRepository.SHORT_ADDRESS,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.85f),
              )
            }
            Button(
              onClick = onOpenMaps,
              colors = ButtonDefaults.buttonColors(containerColor = shopColors.amber),
              shape = RoundedCornerShape(50),
              modifier = Modifier.height(48.dp).testTag("contact_maps_button"),
            ) {
              Icon(
                Icons.Filled.Directions,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text("Directions")
            }
          }
        }
      }
    }

    // 4. Inquiry Form (Pre-filled WhatsApp Send)
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp),
      ) {
        Column(
          modifier = Modifier.fillMaxWidth().padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          Text(
            text = "Have a Question? Send an Inquiry",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
          )
          Text(
            text =
              "Fill your details below and tap Send — we'll open WhatsApp with your request pre-filled so Pradeep ji can reply right away.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )

          OutlinedTextField(
            value = form.name,
            onValueChange = onNameChange,
            label = { Text("Your Name") },
            isError = form.nameError != null,
            supportingText = form.nameError?.let { err -> { Text(err) } },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("inquiry_name_input"),
          )

          OutlinedTextField(
            value = form.phone,
            onValueChange = onPhoneChange,
            label = { Text("Mobile Number") },
            isError = form.phoneError != null,
            supportingText = form.phoneError?.let { err -> { Text(err) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("inquiry_phone_input"),
          )

          ExposedDropdownMenuBox(
            expanded = serviceDropdownExpanded,
            onExpandedChange = { serviceDropdownExpanded = !serviceDropdownExpanded },
          ) {
            OutlinedTextField(
              value = form.selectedService,
              onValueChange = {},
              readOnly = true,
              label = { Text("Service Needed") },
              trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = serviceDropdownExpanded)
              },
              shape = RoundedCornerShape(12.dp),
              modifier =
                Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                  .fillMaxWidth()
                  .testTag("inquiry_service_dropdown"),
            )
            ExposedDropdownMenu(
              expanded = serviceDropdownExpanded,
              onDismissRequest = { serviceDropdownExpanded = false },
            ) {
              ShopCatalogRepository.inquiryServiceOptions.forEach { option ->
                DropdownMenuItem(
                  text = { Text(option) },
                  onClick = {
                    onServiceSelected(option)
                    serviceDropdownExpanded = false
                  },
                )
              }
            }
          }

          OutlinedTextField(
            value = form.message,
            onValueChange = onMessageChange,
            label = { Text("Message / Document Details (Optional)") },
            minLines = 3,
            maxLines = 5,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().testTag("inquiry_message_input"),
          )

          if (form.isSubmitted) {
            Surface(
              color = shopColors.emeraldContainer,
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth(),
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
              ) {
                Icon(
                  Icons.Filled.CheckCircle,
                  contentDescription = null,
                  tint = shopColors.emerald,
                )
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = "Saved & Prepared for WhatsApp!",
                    style = MaterialTheme.typography.titleSmall,
                    color = shopColors.emerald,
                  )
                  Text(
                    text = "Saved on this device and ready on WhatsApp.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                  )
                }
              }
            }
            OutlinedButton(
              onClick = onResetConfirmation,
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth().height(48.dp),
            ) {
              Text("Send Another Inquiry")
            }
          } else {
            Button(
              onClick = onSubmitInquiry,
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth().height(50.dp).testTag("inquiry_submit_button"),
            ) {
              Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text("Save & Send via WhatsApp")
            }
          }
        }
      }
    }

    // 4.5 My Saved Inquiries
    item {
      SectionHeader(
        tagText = "Inquiry History",
        titlePrefix = "My Saved ",
        titleHighlight = "Inquiries",
        subtitle = "Track your past service requests and mark them completed once done.",
        modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp),
      )
    }

    when (val inquiriesState = uiState.inquiriesState) {
      is CloudUiState.Loading -> {
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp),
          ) {
            Row(
              modifier = Modifier.fillMaxWidth().padding(18.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
              CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.5.dp)
              Text(
                text = "Loading your saved inquiries...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }
        }
      }
      is CloudUiState.Error -> {
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors =
              CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
            modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp),
          ) {
            Text(
              text = inquiriesState.message,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onErrorContainer,
              modifier = Modifier.padding(16.dp),
            )
          }
        }
      }
      is CloudUiState.Success -> {
        if (inquiriesState.data.isEmpty()) {
          item {
            Card(
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
              modifier =
                Modifier.fillMaxWidth().widthIn(max = 760.dp).testTag("empty_inquiries_card"),
            ) {
              Text(
                text =
                  "No saved inquiries yet. Submit the form above to save your request and send it on WhatsApp.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(16.dp),
              )
            }
          }
        } else {
          items(inquiriesState.data, key = { it.id }) { inquiry ->
            SavedInquiryCard(
              inquiry = inquiry,
              onMarkCompleted = { onMarkInquiryCompleted(inquiry.id) },
              onDelete = { onDeleteInquiry(inquiry.id) },
              modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp),
            )
          }
        }
      }
    }

    // 5. Footer Credit
    item {
      Text(
        text =
          "© 2026 R K Gallery & Stationers • Proprietor: Pradeep Kanojiya • Shop No.1, Gour Nadi, Jamtara, MP",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(vertical = 8.dp),
      )
    }
  }
}

@Composable
private fun ContactInfoItem(
  icon: ImageVector,
  label: String,
  value: String,
  iconBg: Color = MaterialTheme.colorScheme.primary,
) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    modifier = Modifier.fillMaxWidth(),
  ) {
    Row(
      modifier = Modifier.padding(14.dp),
      verticalAlignment = Alignment.Top,
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Box(
        modifier = Modifier.size(40.dp).clip(RoundedCornerShape(11.dp)).background(iconBg),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier.size(20.dp),
        )
      }
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = label,
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
          text = value,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurface,
        )
      }
    }
  }
}

@Composable
private fun SavedInquiryCard(
  inquiry: ServiceInquiry,
  onMarkCompleted: () -> Unit,
  onDelete: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val shopColors = ShopTheme.colors
  val status = InquiryStatus.fromRaw(inquiry.status)
  val (statusBg, statusFg) =
    when (status) {
      InquiryStatus.OPEN -> shopColors.amberContainer to shopColors.amber
      InquiryStatus.CONTACTED ->
        MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.primary
      InquiryStatus.COMPLETED -> shopColors.emeraldContainer to shopColors.emerald
    }

  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)),
    modifier = modifier.testTag("saved_inquiry_card_${inquiry.id}"),
  ) {
    Column(
      modifier = Modifier.fillMaxWidth().padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Surface(color = statusBg, shape = RoundedCornerShape(50)) {
          Text(
            text = status.displayLabel.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = statusFg,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
          )
        }
        IconButton(
          onClick = onDelete,
          modifier =
            Modifier.minimumInteractiveComponentSize()
              .testTag("delete_inquiry_${inquiry.id}"),
        ) {
          Icon(
            Icons.Filled.DeleteOutline,
            contentDescription = "Delete inquiry",
            tint = MaterialTheme.colorScheme.error,
          )
        }
      }

      Text(
        text = inquiry.serviceName,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
      )
      Text(
        text = "${inquiry.customerName} • ${inquiry.phone}",
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
      )
      if (inquiry.message.isNotBlank()) {
        Text(
          text = inquiry.message,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 3,
          overflow = TextOverflow.Ellipsis,
        )
      }

      if (status != InquiryStatus.COMPLETED) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
        OutlinedButton(
          onClick = onMarkCompleted,
          border = BorderStroke(1.dp, shopColors.emerald),
          shape = RoundedCornerShape(12.dp),
          modifier =
            Modifier.fillMaxWidth().height(48.dp).testTag("complete_inquiry_${inquiry.id}"),
        ) {
          Icon(
            Icons.Filled.DoneAll,
            contentDescription = null,
            tint = shopColors.emerald,
            modifier = Modifier.size(18.dp),
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text("Mark Completed", color = shopColors.emerald)
        }
      }
    }
  }
}
