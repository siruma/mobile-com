package com.homework.data

import com.homework.di.ApplicationScope
import com.homework.di.DefaultDispatcher
import com.homework.model.AppMessage
import com.homework.model.AppUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
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
  private val appDao: AppDao,
  @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
  @ApplicationScope private val scope: CoroutineScope,
) : AppRepository {

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
}
