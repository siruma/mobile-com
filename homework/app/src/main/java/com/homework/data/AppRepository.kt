package com.homework.data

import com.homework.model.AppMessage
import com.homework.model.AppUser
import kotlinx.coroutines.flow.Flow

/**
 * Interface for App repository.
 */
interface AppRepository {

  val activeSensorDataFlow: Flow<Boolean>

  val gyroSensorDataFlow: Flow<IntArray>

  val stepCounterSensorDataFlow: Flow<Int>

  /**
   * All app users.
   */
  val allUser: Flow<List<AppUser>>

  /**
   * All app messages.
   */
  val allMessages: Flow<List<AppMessage>>

  /**
   * Getter app user by ID.
   *
   * @param userId user ID
   */
  suspend fun getAppUser(userId: Int): AppUser?

  /**
   * Create new app user.
   *
   * @param userName New user name
   * @param userEmail New user email
   */
  suspend fun createAppUser(userName: String, userEmail: String): Int

  /**
   * Update app user
   *
   * @param user App user data
   */
  suspend fun updateAppUser(user: AppUser)

  /**
   * Delete all app users.
   */
  suspend fun deleteAppUsers()

  /**
   * Delete app user by ID.
   *
   * @param userId App user ID
   */
  suspend fun deleteAppUser(userId: Int)

  /**
   * Update notifications.
   *
   * @param chatId Chat ID
   */
  suspend fun updateNotification(chatId: Int = 1)

  suspend fun updateNotification(chatId: Int = 1, body:String, timeStamp: Long)

  suspend fun setSensorTimeStamp(timeStamp: Long)

  suspend fun setGyroRate(intArray: IntArray)

  suspend fun setActiveSensorDataService(b: Boolean)

  suspend fun setSteps(steps: Int)

}
