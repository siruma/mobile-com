package com.homework.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_messages")
data class AppMessage(
  @PrimaryKey @ColumnInfo(name = "message_time_stamp") val messageTimestamp: String,
  @ColumnInfo(name = "author")val authorId: Int,
  @ColumnInfo(name = "body")val body: String
)
