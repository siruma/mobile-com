package com.homework.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.homework.R

@Entity(tableName = "app_messages")
data class AppMessage(
  @PrimaryKey(autoGenerate = true) val id: Long,
  @ColumnInfo(name = "message_time_stamp") val messageTimestamp: String,
  @ColumnInfo(name = "author")val authorId: Int,
  @ColumnInfo(name = "author_name") val authorName: String = "",
  @ColumnInfo(name = "body")val body: String,
  // Default profile picture
  @ColumnInfo(name = "author_image") val authorImage: Int = R.drawable.dalle_2025_01_15__japan_game,
  @ColumnInfo(name = "media_uri") val mediaUri: String? = null,
  @ColumnInfo(name = "media_mime_type") val mediaMimeType: String? = null,
  @ColumnInfo(name = "chat_id") val chatId: Int = 1
){
  val isIncoming: Boolean
    get() = authorId != 0
}
