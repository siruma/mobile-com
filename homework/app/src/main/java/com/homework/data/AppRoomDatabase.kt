package com.homework.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.homework.model.AppMessage
import com.homework.model.AppUser
import com.homework.sample.SampleData.conversationSample
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [AppUser::class,AppMessage::class], version = 1)
abstract class AppRoomDatabase : RoomDatabase() {

  abstract fun appDao() : AppDao

  companion object {
    @Volatile
    private var INSTANCE: AppRoomDatabase? = null

    /**
     * Initialize Database.
     */
    fun getDatabase(
      context: Context,
      scope: CoroutineScope
    ): AppRoomDatabase {
      // Return INSTANCE if not null otherwise create database
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppRoomDatabase::class.java,
          "app_database"
        )
          .fallbackToDestructiveMigration()
          .allowMainThreadQueries() //Can lock the UI for a long period of time
          .addCallback(AppDatabaseCallback(scope))
          .build()
        INSTANCE = instance
        // Return
        instance
      }
    }

    private class AppDatabaseCallback(
      private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        INSTANCE?.let { database ->
          scope.launch(Dispatchers.IO) {
            populateDatabase(database.appDao())
          }
        }
      }
    }

    /**
     * Populate database with dummy data.
     *
     * @param appDao App Data Access Object
     */
    suspend fun populateDatabase(appDao: AppDao) {
      var user = AppUser(0, "Maki","maki@japan.game", "")
      appDao.insertUser(user)
      user = AppUser(1, "Sora","Sora@japan.game", "")
      appDao.insertUser(user)
      for (message in conversationSample){
        appDao.insertMessage(message)
      }
    }
  }
}
