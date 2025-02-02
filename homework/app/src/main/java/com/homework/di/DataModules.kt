package com.homework.di

import android.content.Context
import com.homework.data.AppDao
import com.homework.data.AppRepository
import com.homework.data.AppRoomDatabase
import com.homework.data.DefaultAppRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModel {

  @Singleton
  @Binds
  abstract fun bindAppRepository(repository: DefaultAppRepository): AppRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DataModules {

  @Singleton
  @Provides
  fun provideDataBase(@ApplicationContext context: Context,
                      @ApplicationScope scope: CoroutineScope): AppRoomDatabase {
    return AppRoomDatabase.getDatabase(context, scope)
  }

  @Provides
  fun provideAppDao(database: AppRoomDatabase): AppDao = database.appDao()
}
