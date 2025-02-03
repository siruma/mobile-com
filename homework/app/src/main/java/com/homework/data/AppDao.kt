package com.homework.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.homework.model.AppMessage
import com.homework.model.AppUser
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the App User table and message table.
 */
@Dao
interface AppDao {

  /**
   * Getter list of users.
   *
   * @return all users
   */
  @Query("SELECT * FROM app_users ORDER BY user_id ASC")
  fun getUsers(): Flow<List<AppUser>>

  /**
   * Getter a single app user.
   *
   * @param userId the user ID
   * @return the user with userId
   */
  @Query("SELECT * FROM app_users WHERE user_id = :userId")
  fun getUserById(userId : Int): AppUser?

  /**
   * Add new user.
   * On conflict, replace the user.
   *
   * @param appUser new app user
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUser(appUser: AppUser)

  /**
   * Update app user.
   */
  @Update
  suspend fun updateUser(appUser: AppUser)

  /**
   * Delete a user by ID.
   *
   * @param userId the user ID
   */
  @Query("DELETE FROM app_users WHERE user_id = :userId")
  suspend fun deleteUser(userId: Int)

  /**
   * Delete all users.
   */
  @Query("DELETE FROM app_users")
  suspend fun deleteAllUser()

  /**
   * Getter for all messages.
   *
   * @return List of app messages
   */
  @Query("SELECT * FROM app_messages ORDER BY message_time_stamp")
  fun getMessages(): Flow<List<AppMessage>>

  /**
   * Add new message.
   * On conflict, ignore the new message.
   *
   * @param appMessage new message
   */
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertMessage(appMessage: AppMessage)

  /**
   * Delete all messages.
   */
  @Query("DELETE FROM app_messages")
  suspend fun deleteAllMessages()
}
