package com.homework.notification

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import com.homework.MainActivity
import com.homework.R
import com.homework.model.AppMessage
import com.homework.model.AppUser
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationWorker @Inject constructor(@ApplicationContext context: Context) {

  private val appContext = context.applicationContext

  private var notificationManager: NotificationManager =
    context.getSystemService() ?: throw error("NotificationManager service not available")

  /**
   * Set up notification channel.
   */
  fun setUpNotificationChannels(){
    notificationManager = NotificationHandler.setNotificationChannels(
      context = appContext, notificationManager)
  }

  /**
   * Show the notification.
   *
   * @param contact message contact
   * @param messages list of messages
   * @param update if message is update then true
   */
  fun showNotification(
    contact: AppUser,
    messages: List<AppMessage>,
    update: Boolean = false
  ){
    val icon = IconCompat.createWithAdaptiveBitmapContentUri(contact.imgSrc)
    val user = Person.Builder().setName(appContext.getString(R.string.sender_you)).build()
    val person = Person.Builder().setName(contact.username).setIcon(icon).build()

    val messagingStyle = NotificationCompat.MessagingStyle(user)

    val fistId = messages.first().id
    for (message in messages.reversed()) {
      val notificationMessage = NotificationCompat.MessagingStyle.Message(
        message.body,
        parseTimestamp(message.messageTimestamp),
        if(message.isIncoming) person else null,
      ).apply {
        if (message.mediaUri != null) {
          setData(message.mediaMimeType, message.mediaUri.toUri())
        }
      }
      if (message.id == fistId) {
        messagingStyle.addMessage(notificationMessage)
      } else {
        messagingStyle.addHistoricMessage(notificationMessage)
      }
    }

    val notificationBuilder = createNotification(contact, person, messagingStyle)

    if (update) {
      notificationBuilder.setOnlyAlertOnce(true)
    }
    notificationManager.notify(contact.userId, notificationBuilder.build())
  }

  /**
   * Dismiss the chat notification
   *
   * @param chatId ID of chat
   */
  fun dismissNotification(chatId: Int) {
    notificationManager.cancel(chatId)
  }

  @SuppressLint("SuspiciousIndentation")
  private fun createNotification(
    contact: AppUser,
    person: Person,
    messagingStyle: NotificationCompat.MessagingStyle
  ): NotificationCompat.Builder {
    val replyAction: NotificationCompat.Action = generateReplyAction(contact)
    val intent = Intent(appContext, MainActivity::class.java).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val pendingIntent = PendingIntent.getActivity(appContext, REQUEST_CONTENT, intent,
      NotificationHandler.flagUpdateCurrent(false))

      return NotificationCompat.Builder(appContext, NotificationHandler.CHANNEL_MESSAGES)
        .setContentTitle(contact.username)
        .setSmallIcon(R.drawable.ic_message)
        .addPerson(person)
        .setContentIntent(pendingIntent)
        .setStyle(messagingStyle)
        .addAction(replyAction)
        .setShowWhen(true)
  }

  private fun generateReplyAction(
    contact: AppUser
  ): NotificationCompat.Action {
    val timeStamp = Date().time

    val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
      setLabel(appContext.getString(R.string.remote_input_label))
      build()
    }

    val intent = Intent(appContext, NotificationReplyReceiver::class.java)

    intent.apply {
      putExtra(MSG_AUTHOR, contact.username)
      putExtra(MSG_AUTHOR_IMG, contact.imgSrc)
      putExtra(MSG_TIMESTAMP,timeStamp)
    }

    val replyPendingIntent = PendingIntent.getBroadcast(
      appContext,
      REQUEST_CONTENT,
      intent,
      NotificationHandler.flagUpdateCurrent(true)
    )

    // Return the reply action
    return NotificationCompat.Action.Builder(
      IconCompat.createWithResource(appContext, R.drawable.reply_arrow_icon),
      appContext.getString(R.string.rebly_label),
      replyPendingIntent
    )
      .addRemoteInput(remoteInput)
      .setAllowGeneratedReplies(true)
      .setSemanticAction(NotificationCompat.Action.SEMANTIC_ACTION_REPLY)
      .build()

  }

  private fun parseTimestamp(timestamp: String): Long {
    val zonedDateTime = ZonedDateTime.parse(timestamp, DateTimeFormatter.ISO_ZONED_DATE_TIME)
    return zonedDateTime.toInstant().toEpochMilli()
  }

  companion object {
    private const val REQUEST_CONTENT = 1
    const val KEY_TEXT_REPLY = "key_text_reply"
    const val MSG_AUTHOR = "author"
    const val MSG_AUTHOR_IMG = "authorImg"
    const val MSG_TIMESTAMP = "timestamp"
  }
}