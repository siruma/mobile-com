package com.homework.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

@Module
@InstallIn(SingletonComponent::class)
object CoroutinesModule {

  @Provides
  @DefaultDispatcher
  fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default


  @Provides
  @Singleton
  @ApplicationScope
  fun providesCoroutineScope(
    @DefaultDispatcher dispatcher: CoroutineDispatcher
  ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)
}

