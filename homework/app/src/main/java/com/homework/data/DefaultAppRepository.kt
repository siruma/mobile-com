package com.homework.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.homework.di.ApplicationScope
import com.homework.di.DefaultDispatcher
import com.homework.model.AppMessage
import com.homework.model.AppUser
import com.homework.notification.NotificationWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Default app repository. Single entry point for managing App data.
 *
 * @param appDao The app Data access object
 * @param dispatcher The dispatcher to be used for long running or complex operations, such as ID generation.
 * @param scope  The coroutine scope used for deferred jobs where the result isn't important
 */
@Singleton
class DefaultAppRepository @Inject constructor(
  @ApplicationContext private val context: Context,
  private val appDao: AppDao,
  private val notificationWorker: NotificationWorker,
  @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
  @ApplicationScope private val scope: CoroutineScope,
) : AppRepository {

  init {
    notificationWorker.setUpNotificationChannels()
  }

  private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "sensor_data_datastore"
  )

  override val activeSensorDataFlow: Flow<Boolean> = context.dataStore.data.map {
      it[ACTIVE_SENSOR_KEY] ?: false
    }

  override val gyroSensorDataFlow: Flow<IntArray>
    get() = context.dataStore.data.map {
        intArrayOf(
          it[GYRO_X_KEY]?: 0,
          it[GYRO_Y_KEY]?: 0,
          it[GYRO_Z_KEY]?: 0
        )
    }

  override val lightSensorDataFlow: Flow<Int>
    get() = context.dataStore.data.map {
      it[LIGHT_SENSOR] ?: 0
    }

  override val allUser : Flow<List<AppUser>> = appDao.getUsers()

  override val allMessages: Flow<List<AppMessage>> = appDao.getMessages()

  override suspend fun getAppUser(userId: Int): AppUser? {
    return appDao.getUserById(userId)
  }

  override suspend fun createAppUser(userName: String, userEmail: String): Int {
    val userId = withContext(dispatcher){
      UUID.randomUUID().hashCode()
    }
    val user = AppUser(
      userId = userId,
      username = userName,
      userEmail = userEmail
    )
    appDao.insertUser(user)
    return userId
  }

  override suspend fun updateAppUser(user: AppUser) {
    appDao.updateUser(user)
  }

  override suspend fun deleteAppUsers() {
    appDao.deleteAllUser()
  }

  override suspend fun deleteAppUser(userId: Int) {
    appDao.deleteUser(userId)
  }

  override suspend fun updateNotification(chatId: Int) {
    val detail = appDao.getUserById(chatId) ?: return
    val messages = appDao.loadAllMessages(chatId)
    notificationWorker.showNotification(
      detail,
      messages,
      update = true
    )
  }

  override suspend fun updateNotification(chatId: Int, body: String, timeStamp: Long) {
    val detail = appDao.getUserById(chatId) ?: return
    val timeStampString = longToDateTime(timeStamp)
    val messages = List(1) { AppMessage( 0L,
      timeStampString,
      authorId = 2,
      authorName = "Gyro",
      body = body,
      chatId = chatId
    )}
    notificationWorker.showNotification(
      detail,
      messages,
      update = true
    )
  }

  override suspend fun setActiveSensorDataService(b: Boolean) {
    context.dataStore.edit {
      it[ACTIVE_SENSOR_KEY] = b
    }
  }

  override suspend fun setGyroRate(intArray: IntArray) {
    context.dataStore.edit {
      it[GYRO_X_KEY] = intArray[0]
      it[GYRO_Y_KEY] = intArray[1]
      it[GYRO_Z_KEY] = intArray[2]
    }
  }

  override suspend fun setSensorTimeStamp(timeStamp: Long) {
    context.dataStore.edit {
      it[TIME_STAMP_KEY] = timeStamp
    }
  }

  override suspend fun setLightValue(lux: Int) {
    context.dataStore.edit {
      it[LIGHT_SENSOR] = lux
    }
  }

  private fun longToDateTime(long: Long): String {
    val instant = Instant.ofEpochMilli(long)
    val zonedDateTime = instant.atZone(ZoneId.of("+02:00"))
    return zonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
  }

  companion object{
    private val ACTIVE_SENSOR_KEY = booleanPreferencesKey("active_sensor")
    private val TIME_STAMP_KEY = longPreferencesKey("time_stamp")
    private val GYRO_X_KEY = intPreferencesKey("gyro_x")
    private val GYRO_Y_KEY = intPreferencesKey("gyro_y")
    private val GYRO_Z_KEY = intPreferencesKey("gyro_z")
    private val LIGHT_SENSOR = intPreferencesKey("light_sensor")

  }
}
