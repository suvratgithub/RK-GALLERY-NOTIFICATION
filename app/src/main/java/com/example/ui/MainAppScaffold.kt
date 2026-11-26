package com.example.ui

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.AppTab
import com.example.data.BookmarkType
import com.example.data.LocalShopRepository
import com.example.ui.components.LiveUpdatesTicker
import com.example.ui.components.ServiceTagActionSheet
import com.example.ui.components.ShopTopHeader
import com.example.ui.screens.AlertsScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ServicesScreen
import com.example.util.AlertNotificationHelper
import com.example.util.IntentLauncher

@Composable
fun MainAppScaffold(
  viewModel: MainViewModel,
  notificationHelper: AlertNotificationHelper,
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val context = LocalContext.current
  val navController = rememberNavController()
  val backStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = backStackEntry?.destination?.route
  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(uiState.cloudFeedbackMessage) {
    val msg = uiState.cloudFeedbackMessage
    if (msg != null) {
      snackbarHostState.showSnackbar(msg)
      viewModel.clearCloudFeedback()
    }
  }

  // Keep NavHost synced whenever ViewModel requests a tab change
  LaunchedEffect(uiState.selectedTab) {
    if (currentRoute != uiState.selectedTab.route) {
      navController.navigate(uiState.selectedTab.route) {
        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
      }
    }
  }

  // Keep ViewModel synced when hardware back button pops NavHost back to Home
  LaunchedEffect(currentRoute) {
    val matchingTab = AppTab.entries.find { it.route == currentRoute }
    if (matchingTab != null && matchingTab != uiState.selectedTab) {
      viewModel.selectTab(matchingTab)
    }
  }

  val notificationPermissionLauncher =
    rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
      if (granted) {
        viewModel.setAlertsEnabled(true)
        notificationHelper.showAlertsActivatedNotification()
        Toast.makeText(context, "Mobile alerts enabled!", Toast.LENGTH_SHORT).show()
      } else {
        viewModel.setAlertsEnabled(false)
        Toast.makeText(
          context,
          "Notification permission denied. Join our WhatsApp Group for daily alerts!",
          Toast.LENGTH_LONG,
        )
          .show()
      }
    }

  val handleToggleAlerts: (Boolean) -> Unit = { enable ->
    if (!enable) {
      viewModel.setAlertsEnabled(false)
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
      !notificationHelper.hasNotificationPermission()
    ) {
      notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    } else {
      viewModel.setAlertsEnabled(true)
      notificationHelper.showAlertsActivatedNotification()
      Toast.makeText(context, "Mobile alerts enabled!", Toast.LENGTH_SHORT).show()
    }
  }

  BoxWithConstraints(modifier = modifier.fillMaxSize()) {
    val useNavigationRail = maxWidth >= 600.dp

    Row(modifier = Modifier.fillMaxSize()) {
      if (useNavigationRail) {
        NavigationRail(
          containerColor = MaterialTheme.colorScheme.surface,
          modifier = Modifier.fillMaxHeight().statusBarsPadding(),
        ) {
          Spacer(modifier = Modifier.weight(0.1f))
          AppTab.entries.forEach { tab ->
            val selected = uiState.selectedTab == tab
            NavigationRailItem(
              selected = selected,
              onClick = { viewModel.selectTab(tab) },
              icon = {
                Icon(
                  imageVector = tabIcon(tab, selected),
                  contentDescription = tab.label,
                )
              },
              label = { Text(tab.label) },
              modifier = Modifier.testTag("nav_rail_${tab.route}"),
            )
          }
          Spacer(modifier = Modifier.weight(1f))
        }
      }

      Scaffold(
        modifier = Modifier.weight(1f).fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
          Column(modifier = Modifier.statusBarsPadding()) {
            ShopTopHeader(
              alertsEnabled = uiState.alertsEnabled,
              onAlertsClick = { viewModel.jumpToAlerts() },
              onInstagramClick = { IntentLauncher.openInstagram(context) },
            )
            LiveUpdatesTicker(
              headlines = uiState.tickerHeadlines,
              onTickerClick = { viewModel.jumpToAlerts() },
            )
          }
        },
        bottomBar = {
          if (!useNavigationRail) {
            NavigationBar(
              containerColor = MaterialTheme.colorScheme.surface,
              tonalElevation = 4.dp,
            ) {
              AppTab.entries.forEach { tab ->
                val selected = uiState.selectedTab == tab
                NavigationBarItem(
                  selected = selected,
                  onClick = { viewModel.selectTab(tab) },
                  icon = {
                    Icon(
                      imageVector = tabIcon(tab, selected),
                      contentDescription = tab.label,
                    )
                  },
                  label = { Text(tab.label) },
                  modifier = Modifier.testTag("nav_bar_${tab.route}"),
                )
              }
            }
          }
        },
      ) { innerPadding ->
        NavHost(
          navController = navController,
          startDestination = AppTab.HOME.route,
          modifier = Modifier.fillMaxSize().padding(innerPadding),
        ) {
          composable(AppTab.HOME.route) {
            HomeScreen(
              uiState = uiState,
              onExploreServices = { viewModel.selectTab(AppTab.SERVICES) },
              onCallShop = { IntentLauncher.dialShop(context) },
              onWhatsAppDirect = { IntentLauncher.openWhatsAppDirect(context) },
              onWhatsAppGroup = { IntentLauncher.openWhatsAppGroup(context) },
              onOpenInstagram = { IntentLauncher.openInstagram(context) },
              onOpenMaps = { IntentLauncher.openGoogleMaps(context) },
              onOpenBookmark = { bookmark ->
                if (BookmarkType.fromRaw(bookmark.itemType) == BookmarkType.ALERT) {
                  viewModel.jumpToAlerts()
                } else {
                  viewModel.openServiceSheet(bookmark.categoryTitle, bookmark.itemTitle)
                }
              },
              onRemoveBookmark = viewModel::removeBookmark,
            )
          }
          composable(AppTab.SERVICES.route) {
            ServicesScreen(
              uiState = uiState,
              onSearchChange = viewModel::updateServiceSearch,
              onToggleCategory = viewModel::toggleCategory,
              onExpandAllToggle = viewModel::toggleExpandAll,
              onServiceTagClick = viewModel::openServiceSheet,
              onCallShop = { IntentLauncher.dialShop(context) },
              onWhatsAppGroup = { IntentLauncher.openWhatsAppGroup(context) },
            )
          }
          composable(AppTab.ALERTS.route) {
            AlertsScreen(
              uiState = uiState,
              onSelectFilter = viewModel::selectAlertCategory,
              onToggleAlerts = handleToggleAlerts,
              onApplyWhatsApp = { prefill -> IntentLauncher.openWhatsAppDirect(context, prefill) },
              onJoinWhatsAppGroup = { IntentLauncher.openWhatsAppGroup(context) },
              onCallShop = { IntentLauncher.dialShop(context) },
              onToggleAlertBookmark = viewModel::toggleAlertBookmark,
            )
          }
          composable(AppTab.CONTACT.route) {
            ContactScreen(
              uiState = uiState,
              onCallShop = { IntentLauncher.dialShop(context) },
              onWhatsAppDirect = { IntentLauncher.openWhatsAppDirect(context) },
              onWhatsAppGroup = { IntentLauncher.openWhatsAppGroup(context) },
              onOpenInstagram = { IntentLauncher.openInstagram(context) },
              onOpenMaps = { IntentLauncher.openGoogleMaps(context) },
              onNameChange = viewModel::updateInquiryName,
              onPhoneChange = viewModel::updateInquiryPhone,
              onServiceSelected = viewModel::updateInquiryService,
              onMessageChange = viewModel::updateInquiryMessage,
              onSubmitInquiry = {
                val message = viewModel.validateAndBuildWhatsAppInquiry()
                if (message != null) {
                  IntentLauncher.openWhatsAppDirect(context, message)
                }
              },
              onResetConfirmation = viewModel::resetInquiryConfirmation,
              onMarkInquiryCompleted = viewModel::markInquiryCompleted,
              onDeleteInquiry = viewModel::deleteInquiry,
            )
          }
        }
      }
    }
  }

  val sheetContext = uiState.selectedServiceSheet
  if (sheetContext != null) {
    val savedBookmarkIds =
      (uiState.bookmarksState as? CloudUiState.Success)?.data?.map { it.id }?.toSet().orEmpty()
    val rawKey = "${sheetContext.categoryTitle}_${sheetContext.serviceName}"
    val isSaved =
      LocalShopRepository.sanitizeBookmarkId(BookmarkType.SERVICE, rawKey) in savedBookmarkIds
    ServiceTagActionSheet(
      context = sheetContext,
      isBookmarked = isSaved,
      onToggleBookmark = {
        viewModel.toggleServiceBookmark(
          categoryTitle = sheetContext.categoryTitle,
          serviceName = sheetContext.serviceName,
          currentlySaved = isSaved,
        )
      },
      onDismiss = viewModel::dismissServiceSheet,
      onWhatsAppClick = {
        viewModel.dismissServiceSheet()
        IntentLauncher.openWhatsAppDirect(
          context,
          "Namaste, mujhe '${sheetContext.serviceName}' (${sheetContext.categoryTitle}) ke baare mein jaankari chahiye.",
        )
      },
      onPrefillContactClick = {
        viewModel.prefillInquiryFromService(sheetContext.serviceName)
      },
      onCallShopClick = {
        viewModel.dismissServiceSheet()
        IntentLauncher.dialShop(context)
      },
    )
  }
}

private fun tabIcon(tab: AppTab, selected: Boolean): ImageVector =
  when (tab) {
    AppTab.HOME -> if (selected) Icons.Filled.Home else Icons.Outlined.Home
    AppTab.SERVICES -> if (selected) Icons.Filled.Apps else Icons.Outlined.Apps
    AppTab.ALERTS -> if (selected) Icons.Filled.Notifications else Icons.Outlined.Notifications
    AppTab.CONTACT -> if (selected) Icons.Filled.Call else Icons.Outlined.Call
  }
