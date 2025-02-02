package com.homework.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_users")
data class AppUser(
  @PrimaryKey @ColumnInfo(name = "user_id") val userId: Int,
  @ColumnInfo(name = "user_name") var username: String,
  @ColumnInfo(name = "user_email") var userEmail: String,
  @ColumnInfo(name = "img_src") var imgSrc: String = ""
)