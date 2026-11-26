package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.data.BookmarkType
import com.example.data.CustomerReview
import com.example.data.GalleryHighlight
import com.example.data.SavedBookmark
import com.example.data.ShopCatalogRepository
import com.example.data.StationeryCategory
import com.example.ui.CloudUiState
import com.example.ui.ShopUiState
import com.example.ui.components.SectionHeader
import com.example.ui.components.mapStationeryIcon
import com.example.ui.theme.ShopTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
  uiState: ShopUiState,
  onExploreServices: () -> Unit,
  onCallShop: () -> Unit,
  onWhatsAppDirect: () -> Unit,
  onWhatsAppGroup: () -> Unit,
  onOpenInstagram: () -> Unit,
  onOpenMaps: () -> Unit,
  onOpenBookmark: (SavedBookmark) -> Unit = {},
  onRemoveBookmark: (String) -> Unit = {},
  modifier: Modifier = Modifier,
) {
  val shopColors = ShopTheme.colors

  BoxWithConstraints(modifier = modifier.fillMaxSize()) {
    val isWide = maxWidth >= 600.dp

    LazyColumn(
      modifier = Modifier.fillMaxSize().testTag("home_screen_list"),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
      verticalArrangement = Arrangement.spacedBy(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      // 1. HERO SECTION
      item {
        Box(
          modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp).padding(top = 8.dp),
        ) {
          Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth(),
          ) {
            Box(
              modifier =
                Modifier.fillMaxWidth()
                  .background(
                    Brush.linearGradient(
                      listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f),
                        shopColors.amberContainer.copy(alpha = 0.7f),
                        shopColors.cyberCyanContainer.copy(alpha = 0.75f),
                      )
                    )
                  )
                  .padding(20.dp)
            ) {
              Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(
                  color = shopColors.amber.copy(alpha = 0.14f),
                  shape = RoundedCornerShape(50),
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                  ) {
                    Icon(
                      Icons.Filled.Storefront,
                      contentDescription = null,
                      tint = shopColors.amber,
                      modifier = Modifier.size(14.dp),
                    )
                    Text(
                      text = "Jamtara's #1 Cyber Cafe & Store",
                      style = MaterialTheme.typography.labelMedium,
                      color = shopColors.amber,
                    )
                  }
                }

                Text(
                  text =
                    buildAnnotatedString {
                      append("Gallery, Stationery &\n")
                      withStyle(SpanStyle(color = shopColors.amber)) {
                        append("300+ Online Services")
                      }
                    },
                  style = MaterialTheme.typography.displaySmall,
                  color = MaterialTheme.colorScheme.primary,
                )

                Text(
                  text =
                    "From Aadhaar to Passport, Govt Job Forms to Bill Payments — every digital & stationery need in one trusted place. Fast internet, expert staff, fair rates.",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                FlowRow(
                  horizontalArrangement = Arrangement.spacedBy(8.dp),
                  verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                  Button(
                    onClick = onExploreServices,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.height(48.dp).testTag("hero_services_button"),
                  ) {
                    Icon(
                      Icons.Filled.Laptop,
                      contentDescription = null,
                      modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("All Services")
                  }
                  OutlinedButton(
                    onClick = onCallShop,
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                    modifier = Modifier.height(48.dp).testTag("hero_call_button"),
                  ) {
                    Icon(
                      Icons.Filled.Call,
                      contentDescription = null,
                      modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Call Now")
                  }
                  Button(
                    onClick = onWhatsAppDirect,
                    colors = ButtonDefaults.buttonColors(containerColor = shopColors.whatsappDark),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.height(48.dp).testTag("hero_whatsapp_button"),
                  ) {
                    Icon(
                      Icons.Filled.Chat,
                      contentDescription = null,
                      modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("WhatsApp")
                  }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                  HeroStat("4.8 ★", "Google Rating")
                  HeroStat("300+", "Services")
                  HeroStat("20", "Categories")
                  HeroStat("12 Hrs", "Daily Open")
                }
              }
            }
          }

          // Floating rating badge overlapping top-right corner
          Surface(
            color = shopColors.amber,
            shape = RoundedCornerShape(50),
            shadowElevation = 6.dp,
            modifier = Modifier.align(Alignment.TopEnd).offset(x = (-8).dp, y = (-10).dp),
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
              Icon(
                Icons.Filled.Star,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp),
              )
              Text(
                text = "4.8 Rated",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
              )
            }
          }
        }
      }

      // 2. SOCIAL & COMMUNITY CARDS (INSTAGRAM + WHATSAPP GROUP)
      item {
        Column(
          modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          // Instagram Feature Card
          Card(
            onClick = onOpenInstagram,
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth().testTag("home_instagram_card"),
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
                  Modifier.size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center,
              ) {
                Icon(
                  Icons.Filled.CameraAlt,
                  contentDescription = "Instagram Logo",
                  tint = Color.White,
                  modifier = Modifier.size(26.dp),
                )
              }
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "Follow Us on Instagram",
                  style = MaterialTheme.typography.titleSmall,
                  color = Color.White,
                )
                Text(
                  text = ShopCatalogRepository.INSTAGRAM_HANDLE,
                  style = MaterialTheme.typography.bodySmall,
                  color = Color.White.copy(alpha = 0.9f),
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                )
              }
              Surface(
                color = Color.White,
                shape = RoundedCornerShape(50),
              ) {
                Text(
                  text = "Follow",
                  style = MaterialTheme.typography.labelMedium,
                  color = shopColors.instagramMid,
                  modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                )
              }
            }
          }

          // WhatsApp Group Banner
          Card(
            onClick = onWhatsAppGroup,
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth().testTag("home_whatsapp_group_card"),
          ) {
            Row(
              modifier =
                Modifier.fillMaxWidth()
                  .background(
                    Brush.linearGradient(listOf(Color(0xFF075E54), shopColors.whatsappDark))
                  )
                  .padding(16.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
              Box(
                modifier =
                  Modifier.size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center,
              ) {
                Icon(
                  Icons.Filled.Groups,
                  contentDescription = "WhatsApp Community",
                  tint = Color.White,
                  modifier = Modifier.size(26.dp),
                )
              }
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "Join Our WhatsApp Group",
                  style = MaterialTheme.typography.titleSmall,
                  color = Color.White,
                )
                Text(
                  text = "Get daily job notifications, scheme alerts & offers free!",
                  style = MaterialTheme.typography.bodySmall,
                  color = Color.White.copy(alpha = 0.88f),
                )
              }
              Surface(color = Color.White, shape = RoundedCornerShape(50)) {
                Text(
                  text = "Join",
                  style = MaterialTheme.typography.labelMedium,
                  color = shopColors.whatsappDark,
                  modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                )
              }
            }
          }
        }
      }

      // 2.5 SAVED BOOKMARKS (CLOUD SYNCED)
      val savedBookmarks = (uiState.bookmarksState as? CloudUiState.Success)?.data.orEmpty()
      if (savedBookmarks.isNotEmpty()) {
        item {
          Column(
            modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
          ) {
            SectionHeader(
              tagText = "Saved Bookmarks",
              titlePrefix = "Your Saved ",
              titleHighlight = "Services & Alerts",
              subtitle = "Quick access to your bookmarked services and job alerts.",
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
              items(savedBookmarks, key = { it.id }) { bookmark ->
                SavedBookmarkCard(
                  bookmark = bookmark,
                  onClick = { onOpenBookmark(bookmark) },
                  onRemove = { onRemoveBookmark(bookmark.id) },
                )
              }
            }
          }
        }
      }

      // 3. STATIONERY & GALLERY PRODUCTS
      item {
        Column(
          modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
          SectionHeader(
            tagText = "Stationery & Gallery",
            titlePrefix = "Our Store ",
            titleHighlight = "Products",
            subtitle =
              "From school essentials to office stationery, art supplies, photo framing, and quick lamination — everything well-stocked.",
          )
          val columns = if (isWide) 3 else 2
          val rows = uiState.stationeryCategories.chunked(columns)
          Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            rows.forEach { rowItems ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
              ) {
                rowItems.forEach { item ->
                  StationeryCard(
                    item = item,
                    modifier = Modifier.weight(1f),
                  )
                }
                repeat(columns - rowItems.size) { Spacer(modifier = Modifier.weight(1f)) }
              }
            }
          }
        }
      }

      // 4. STORE GALLERY HIGHLIGHTS
      item {
        Column(
          modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
          SectionHeader(
            tagText = "Gallery",
            titlePrefix = "Inside ",
            titleHighlight = "Our Store",
            subtitle =
              "A modern cyber cafe and curated stationery gallery serving Jamtara & Bilhari.",
          )
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
          ) {
            items(uiState.galleryHighlights, key = { it.id }) { highlight ->
              GalleryCard(highlight = highlight)
            }
          }
        }
      }

      // 5. ABOUT STORE & PROPRIETOR
      item {
        Column(
          modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
          SectionHeader(
            tagText = "About Us",
            titlePrefix = "Jamtara's Most ",
            titleHighlight = "Trusted Store",
            subtitle =
              "20 categories and 300+ online services alongside complete stationery & art supplies under one roof.",
          )
          Card(
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth(),
          ) {
            Column(
              modifier =
                Modifier.fillMaxWidth()
                  .background(
                    Brush.linearGradient(
                      listOf(MaterialTheme.colorScheme.primary, Color(0xFF3949AB))
                    )
                  )
                  .padding(20.dp),
              verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                  Box(
                    modifier =
                      Modifier.size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.16f)),
                    contentAlignment = Alignment.Center,
                  ) {
                    Icon(
                      Icons.Filled.Storefront,
                      contentDescription = null,
                      tint = Color.White,
                    )
                  }
                  Column {
                    Text(
                      text = ShopCatalogRepository.SHOP_NAME,
                      style = MaterialTheme.typography.titleMedium,
                      color = Color.White,
                    )
                    Text(
                      text = "Jamtara, Madhya Pradesh",
                      style = MaterialTheme.typography.bodySmall,
                      color = Color.White.copy(alpha = 0.8f),
                    )
                  }
                }
                Surface(color = shopColors.amber, shape = RoundedCornerShape(10.dp)) {
                  Text(
                    text = "Since Day 1",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                  )
                }
              }

              Text(
                text = ShopCatalogRepository.ADDRESS,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.88f),
              )

              HorizontalDivider(color = Color.White.copy(alpha = 0.2f))

              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                  Icon(
                    Icons.Filled.Person,
                    contentDescription = null,
                    tint = Color(0xFFFFD180),
                    modifier = Modifier.size(20.dp),
                  )
                  Column {
                    Text(
                      text = "Shop Owner / Proprietor",
                      style = MaterialTheme.typography.labelSmall,
                      color = Color.White.copy(alpha = 0.72f),
                    )
                    Text(
                      text = ShopCatalogRepository.OWNER_NAME,
                      style = MaterialTheme.typography.titleSmall,
                      color = Color.White,
                    )
                  }
                }
                OutlinedButton(
                  onClick = onOpenMaps,
                  border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
                  colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                  shape = RoundedCornerShape(50),
                  modifier = Modifier.height(48.dp).testTag("about_directions_button"),
                ) {
                  Icon(
                    Icons.Filled.Directions,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Directions")
                }
              }
            }
          }

          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            TrustPointRow(
              icon = Icons.Filled.Verified,
              title = "Quality Products",
              desc = "Trusted stationery brands, notebooks, and art materials for every budget.",
            )
            TrustPointRow(
              icon = Icons.Filled.Laptop,
              title = "20 Categories, 300+ Services",
              desc = "Aadhaar, PAN, Voter ID, Job Forms, MP Online & Samagra — all in one shop.",
            )
            TrustPointRow(
              icon = Icons.Filled.Bolt,
              title = "Fast & Reliable Work",
              desc = "High-speed internet and experienced staff ensure accurate, on-time service.",
            )
            TrustPointRow(
              icon = Icons.Filled.Schedule,
              title = "Open 9:00 AM – 9:00 PM",
              desc = "Open 12 hours daily, Monday to Sunday — walk in anytime, no appointment needed.",
            )
          }
        }
      }

      // 6. CUSTOMER REVIEWS
      item {
        Column(
          modifier = Modifier.fillMaxWidth().widthIn(max = 760.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
          SectionHeader(
            tagText = "Customer Reviews",
            titlePrefix = "What Our ",
            titleHighlight = "Customers Say",
            subtitle = "4.8 ★ Google rating — trusted across Jamtara, Bilhari & Gour Nadi.",
          )
          Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            uiState.customerReviews.forEach { review -> ReviewCard(review = review) }
          }
        }
      }

      item { Spacer(modifier = Modifier.height(12.dp)) }
    }
  }
}

@Composable
private fun HeroStat(value: String, label: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = value,
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.ExtraBold,
      color = MaterialTheme.colorScheme.primary,
    )
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}

@Composable
private fun SavedBookmarkCard(
  bookmark: SavedBookmark,
  onClick: () -> Unit,
  onRemove: () -> Unit,
) {
  val shopColors = ShopTheme.colors
  val type = BookmarkType.fromRaw(bookmark.itemType)
  val badgeBg =
    if (type == BookmarkType.ALERT) shopColors.amberContainer
    else MaterialTheme.colorScheme.primaryContainer
  val badgeFg =
    if (type == BookmarkType.ALERT) shopColors.amber else MaterialTheme.colorScheme.primary

  Card(
    onClick = onClick,
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)),
    modifier = Modifier.width(232.dp).testTag("home_bookmark_card_${bookmark.id}"),
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Surface(color = badgeBg, shape = RoundedCornerShape(50)) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
          ) {
            Icon(
              Icons.Filled.Bookmark,
              contentDescription = null,
              tint = badgeFg,
              modifier = Modifier.size(12.dp),
            )
            Text(
              text = type.displayLabel,
              style = MaterialTheme.typography.labelSmall,
              color = badgeFg,
              maxLines = 1,
            )
          }
        }
        IconButton(
          onClick = onRemove,
          modifier =
            Modifier.minimumInteractiveComponentSize()
              .testTag("home_remove_bookmark_${bookmark.id}"),
        ) {
          Icon(
            Icons.Filled.Close,
            contentDescription = "Remove bookmark",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp),
          )
        }
      }
      Text(
        text = bookmark.itemTitle,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
      )
      Text(
        text = bookmark.categoryTitle,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}

@Composable
private fun StationeryCard(item: StationeryCategory, modifier: Modifier = Modifier) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier,
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      Box(
        modifier =
          Modifier.size(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
              Brush.linearGradient(
                listOf(MaterialTheme.colorScheme.primary, Color(0xFF5C6BC0))
              )
            ),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = mapStationeryIcon(item.iconKey),
          contentDescription = item.title,
          tint = Color.White,
          modifier = Modifier.size(22.dp),
        )
      }
      Text(
        text = item.title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
      )
      Text(
        text = item.description,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}

@Composable
private fun GalleryCard(highlight: GalleryHighlight) {
  val shopColors = ShopTheme.colors
  val gradientColors =
    when (highlight.paletteIndex) {
      0 -> listOf(Color(0xFF1A237E), Color(0xFF5C6BC0))
      1 -> listOf(Color(0xFFE65100), Color(0xFFFF8F00))
      2 -> listOf(shopColors.cyberCyan, Color(0xFF004D40))
      3 -> listOf(Color(0xFF880E4F), Color(0xFFE91E63))
      4 -> listOf(shopColors.emerald, Color(0xFF1B5E20))
      else -> listOf(Color(0xFF37474F), Color(0xFF78909C))
    }

  Card(
    shape = RoundedCornerShape(18.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    modifier = Modifier.width(156.dp).height(156.dp),
  ) {
    Box(
      modifier = Modifier.fillMaxSize().background(Brush.linearGradient(gradientColors)).padding(14.dp),
      contentAlignment = Alignment.Center,
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Icon(
          imageVector = mapStationeryIcon(highlight.iconKey),
          contentDescription = highlight.title,
          tint = Color.White,
          modifier = Modifier.size(34.dp),
        )
        Text(
          text = highlight.title,
          style = MaterialTheme.typography.titleSmall,
          color = Color.White,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Text(
          text = highlight.subtitle,
          style = MaterialTheme.typography.labelSmall,
          color = Color.White.copy(alpha = 0.85f),
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
  }
}

@Composable
private fun TrustPointRow(icon: ImageVector, title: String, desc: String) {
  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
    modifier = Modifier.fillMaxWidth(),
  ) {
    Row(
      modifier = Modifier.padding(14.dp),
      verticalAlignment = Alignment.Top,
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Box(
        modifier =
          Modifier.size(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(20.dp),
        )
      }
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleSmall,
          color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
          text = desc,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
  }
}

@Composable
private fun ReviewCard(review: CustomerReview) {
  val shopColors = ShopTheme.colors
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier.fillMaxWidth(),
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        repeat(review.rating) {
          Icon(
            Icons.Filled.Star,
            contentDescription = null,
            tint = shopColors.starGold,
            modifier = Modifier.size(16.dp),
          )
        }
      }
      Text(
        text = "\"${review.quote}\"",
        style = MaterialTheme.typography.bodyMedium,
        fontStyle = FontStyle.Italic,
        color = MaterialTheme.colorScheme.onSurface,
      )
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        Box(
          modifier =
            Modifier.size(38.dp)
              .clip(CircleShape)
              .background(
                Brush.linearGradient(
                  listOf(MaterialTheme.colorScheme.primary, Color(0xFF5C6BC0))
                )
              ),
          contentAlignment = Alignment.Center,
        ) {
          Text(
            text = review.initial,
            style = MaterialTheme.typography.titleSmall,
            color = Color.White,
          )
        }
        Column {
          Text(
            text = review.author,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
          )
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp),
          ) {
            Icon(
              Icons.Filled.LocationOn,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(12.dp),
            )
            Text(
              text = review.location,
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
      }
    }
  }
}
