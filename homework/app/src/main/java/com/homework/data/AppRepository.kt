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

  val lightSensorDataFlow: Flow<Int>

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

  /**
   * Update Notification
   *
   * @param chatId Chat ID
   * @param body Message body
   * @param timeStamp Message time stamp
   */
  suspend fun updateNotification(chatId: Int = 1, body:String, timeStamp: Long)

  /**
   * Setter for sensor time stamp.
   *
   * @param timeStamp Long value of time
   */
  suspend fun setSensorTimeStamp(timeStamp: Long)

  /**
   * Setter for gyro rate,
   *
   * @param intArray gyro rate array
   */
  suspend fun setGyroRate(intArray: IntArray)

  /**
   * Setter for active sensor data service.
   */
  suspend fun setActiveSensorDataService(b: Boolean)

  /**
   * Setter for light sensor data.
   */
  suspend fun setLightValue(steps: Int)

}
