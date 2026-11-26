package com.example.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.data.ShopCatalogRepository
import java.net.URLEncoder

object IntentLauncher {
  fun dialShop(context: Context) {
    val intent =
      Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:${ShopCatalogRepository.PHONE_RAW}")
      }
    launchSafely(context, intent, "Unable to open phone dialer.")
  }

  fun openWhatsAppDirect(context: Context, message: String? = null) {
    val base = "https://wa.me/${ShopCatalogRepository.WHATSAPP_NUMBER}"
    val url =
      if (message.isNullOrBlank()) {
        base
      } else {
        val encoded = URLEncoder.encode(message, "UTF-8")
        "$base?text=$encoded"
      }
    openUrl(context, url, "Unable to open WhatsApp.")
  }

  fun openWhatsAppGroup(context: Context) {
    openUrl(context, ShopCatalogRepository.WHATSAPP_GROUP_URL, "Unable to open WhatsApp group.")
  }

  fun openInstagram(context: Context) {
    openUrl(context, ShopCatalogRepository.INSTAGRAM_URL, "Unable to open Instagram.")
  }

  fun openGoogleMaps(context: Context) {
    openUrl(context, ShopCatalogRepository.MAPS_URL, "Unable to open Google Maps.")
  }

  fun openUrl(context: Context, url: String, fallbackMessage: String = "Unable to open link.") {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    launchSafely(context, intent, fallbackMessage)
  }

  private fun launchSafely(context: Context, intent: Intent, fallbackMessage: String) {
    try {
      context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
      Toast.makeText(context, fallbackMessage, Toast.LENGTH_SHORT).show()
    } catch (_: Exception) {
      Toast.makeText(context, fallbackMessage, Toast.LENGTH_SHORT).show()
    }
  }
}
