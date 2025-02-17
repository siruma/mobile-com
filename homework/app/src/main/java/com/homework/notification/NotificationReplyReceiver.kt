package com.homework.notification

import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.homework.data.DefaultAppRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationReplyReceiver : BroadcastReceiver() {

  @Inject
  lateinit var repository: DefaultAppRepository

  override fun onReceive(context: Context, intent: Intent) {
    val result = RemoteInput.getResultsFromIntent(intent) ?: return
    val input = result.getCharArray(KEY_TEXT_REPLY).toString()
    val uri = intent.data // Should ?: return
    val chatId = uri?.lastPathSegment?.toInt() ?: 1 //When more chats should be return

    if (chatId > 0 && input.isNotBlank()) {
      val pendingResult = goAsync()
      val job = Job()
      CoroutineScope(job).launch {
        try {
          // FIXME: create SendMessage function into DefaultAppRepository
          //repository.sendMessage(chatId, input, null, null)
          repository.updateNotification(chatId)
        } finally {
          pendingResult.finish()
        }
      }
    }
  }


  companion object {
    const val KEY_TEXT_REPLY = "reply"
  }
}
