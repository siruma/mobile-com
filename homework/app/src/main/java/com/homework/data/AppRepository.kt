package com.homework.data

import com.homework.model.AppMessage
import com.homework.model.AppUser
import kotlinx.coroutines.flow.Flow

interface AppRepository {

  val allUser: Flow<List<AppUser>>

  val allMessages: Flow<List<AppMessage>>

  suspend fun getAppUser(userId: Int): AppUser?

  suspend fun createAppUser(userName: String, userEmail: String): Int

  suspend fun updateAppUser(user: AppUser)

  suspend fun deleteAppUsers()

  suspend fun deleteAppUser(userId: Int)

}
