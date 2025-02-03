package com.homework

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.homework.data.AppDao
import com.homework.data.AppRoomDatabase
import com.homework.model.AppMessage
import com.homework.model.AppUser
import com.homework.testUtils.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class AppDaoTest {

  private lateinit var appDao: AppDao
  private lateinit var db: AppRoomDatabase

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

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

  @Test
  fun deleteUsersAndGetAppUser() = runBlocking {
    appDao.insertUser(AppUser(1,"Test", "test@test.com", ""))

    // Delete users
    appDao.deleteAllUser()

    val users = appDao.getUsers().asLiveData().getOrAwaitValue()
    Assert.assertEquals(true, users.isEmpty())
  }

  @Test
  fun insertMessageAndGetMessage() = runBlocking {
    val appMessage = AppMessage("2025-03-02T18:11:37+02:00", 0, "Test message")
    appDao.insertMessage(appMessage)
    val messages = appDao.getMessages().asLiveData().getOrAwaitValue()
    val message = messages[0]
    Assert.assertEquals(appMessage, message)
  }

  @Test
  fun deleteMessagesAndGetMessage() = runBlocking {
    val appMessage = AppMessage("2025-03-02T18:11:37+02:00", 0, "Test message")
    appDao.insertMessage(appMessage)

    // Delete all messages
    appDao.deleteAllMessages()

    // Then the list is empty
    val message = appDao.getMessages().asLiveData().getOrAwaitValue()
    Assert.assertEquals(true, message.isEmpty())
  }
}
