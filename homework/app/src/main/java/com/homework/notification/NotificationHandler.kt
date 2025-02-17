package com.homework.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import com.homework.MainActivity
import com.homework.R

object NotificationHandler {

  const val CHANNEL_MESSAGES: String = "chatNotificationChannel"
  private const val CATEGORY_SHARE: String = "FIXME"

  fun flagUpdateCurrent(mutable: Boolean): Int {
    return if (mutable) {
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
    } else {
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    }
  }

  fun setNotificationChannels(context: Context,
                              notificationManager: NotificationManager): NotificationManager {
    if (notificationManager.getNotificationChannel(CHANNEL_MESSAGES) == null) {
      notificationManager.createNotificationChannel(
        NotificationChannel(
          CHANNEL_MESSAGES,
          context.getString(R.string.channel_new_message),
          NotificationManager.IMPORTANCE_HIGH
        )
      )
    }
    return notificationManager
  }

  fun generateNotificationId(): Int {
    TODO()
  }

  fun generateShortcut(context: Context, shortcutId: String): ShortcutInfoCompat {
    val shortcutInfoCompat = ShortcutInfoCompat.Builder(context, shortcutId)
      .setCategories(setOf(CATEGORY_SHARE))
      .setLongLived(true)
      .setShortLabel(shortcutId)
      .setLongLabel(shortcutId)
      .setIntent(Intent(context, MainActivity::class.java).setAction(Intent.ACTION_VIEW))
      .build()
    return shortcutInfoCompat
  }

}
