package com.homework

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.homework.data.AppDao
import com.homework.data.AppRoomDatabase
import com.homework.model.AppUser
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class AppDaoTest {

  private lateinit var appDao: AppDao
  private lateinit var db: AppRoomDatabase

  @Before
  fun beforeTest() {
    val context : Context = ApplicationProvider.getApplicationContext()
    db = Room.inMemoryDatabaseBuilder(context, AppRoomDatabase::class.java)
      .allowMainThreadQueries()
      .build()
    appDao = db.appDao()
  }

  @After
  @Throws(IOException::class)
  fun afterTest() {
    db.close()
  }

  @Test
  @Throws(Exception::class)
  fun insertUserAndGetAppUser() = runBlocking {
    val appUser = AppUser(1,"Test", "test@test.com", "")
    appDao.insertUser(appUser)
    val appUser1 = appDao.getUserById(1)
    Assert.assertEquals(appUser1,appUser)
  }
}
