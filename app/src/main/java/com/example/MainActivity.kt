package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.data.AlertPreferencesRepository
import com.example.data.LocalShopRepository
import com.example.ui.MainAppScaffold
import com.example.ui.MainViewModel
import com.example.ui.theme.RKGalleryTheme
import com.example.util.AlertNotificationHelper

class MainActivity : ComponentActivity() {
  private val alertPrefsRepo by lazy { AlertPreferencesRepository(applicationContext) }
  private val localShopRepo by lazy { LocalShopRepository(applicationContext) }
  private val notificationHelper by lazy { AlertNotificationHelper(applicationContext) }
  private val viewModel: MainViewModel by viewModels {
    MainViewModel.Factory(
      alertPrefsRepo = alertPrefsRepo,
      cloudStore = localShopRepo,
      currentUserId = "local_user",
      userDisplayName = "Customer",
      userEmail = null,
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    notificationHelper.ensureChannelCreated()
    enableEdgeToEdge()
    setContent {
      RKGalleryTheme {
        MainAppScaffold(
          viewModel = viewModel,
          notificationHelper = notificationHelper,
        )
      }
    }
  }
}
