package com.example.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R

class AlertNotificationHelper(private val context: Context) {
  companion object {
    const val CHANNEL_ID = "rk_gallery_job_alerts"
    private const val CONFIRMATION_NOTIF_ID = 1001
  }

  fun ensureChannelCreated() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val name = context.getString(R.string.notif_channel_name)
      val descriptionText = context.getString(R.string.notif_channel_desc)
      val importance = NotificationManager.IMPORTANCE_DEFAULT
      val channel =
        NotificationChannel(CHANNEL_ID, name, importance).apply { description = descriptionText }
      val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      manager.createNotificationChannel(channel)
    }
  }

  fun hasNotificationPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
        PackageManager.PERMISSION_GRANTED
    } else {
      true
    }
  }

  fun showAlertsActivatedNotification() {
    ensureChannelCreated()
    if (!hasNotificationPermission()) return

    val launchIntent =
      Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
      }
    val pendingIntent =
      PendingIntent.getActivity(
        context,
        0,
        launchIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
      )

    val notification =
      NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notif_bell)
        .setContentTitle("R K Gallery & Stationers • Alerts Active")
        .setContentText(
          "Daily MP job, admit card, and sarkari yojana alerts are now enabled on your device."
        )
        .setStyle(
          NotificationCompat.BigTextStyle()
            .bigText(
              "Mobile alerts are active! Check MP Police 2026, SSC GD, Railway RPF, and Ladli Behna e-KYC updates anytime in the Alerts tab."
            )
        )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    try {
      NotificationManagerCompat.from(context).notify(CONFIRMATION_NOTIF_ID, notification)
    } catch (_: SecurityException) {
      // Permission revoked externally
    }
  }
}
