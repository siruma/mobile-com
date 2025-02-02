package com.homework.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.homework.model.AppMessage
import com.homework.model.AppUser
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

  @Query("SELECT * FROM app_users ORDER BY user_id ASC")
  fun getUsers(): Flow<List<AppUser>>

  @Query("SELECT * FROM app_users WHERE user_id = :userId")
  fun getUserById(userId : Int): AppUser?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUser(appUser: AppUser)

  @Update
  suspend fun updateUser(appUser: AppUser)

  @Query("DELETE FROM app_users WHERE user_id = :userId")
  suspend fun deleteUser(userId: Int)

  @Query("DELETE FROM app_users")
  suspend fun deleteAll()

  @Query("SELECT * FROM app_messages ORDER BY message_time_stamp")
  fun getMessages(): Flow<List<AppMessage>>

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertMessage(appMessage: AppMessage)
}
