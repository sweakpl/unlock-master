package com.sweak.unlockmaster.di

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import com.sweak.unlockmaster.data.local.database.UnlockMasterDatabase
import com.sweak.unlockmaster.data.local.database.dao.UnlockEventsDao
import com.sweak.unlockmaster.data.repository.TimeRepositoryImpl
import com.sweak.unlockmaster.data.repository.UnlockEventsRepositoryImpl
import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.repository.UnlockEventsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideNotificationManager(app: Application): NotificationManagerCompat =
        NotificationManagerCompat.from(app)

    @Provides
    @Singleton
    fun provideUnlockMasterDatabase(app: Application): UnlockMasterDatabase =
        Room.databaseBuilder(
            app.applicationContext,
            UnlockMasterDatabase::class.java,
            "unlock_master_database"
        ).build()

    @Provides
    @Singleton
    fun provideUnlockEventsDao(database: UnlockMasterDatabase): UnlockEventsDao =
        database.unlockEventsDao()

    @Provides
    @Singleton
    fun provideUnlockEventsRepository(unlockEventsDao: UnlockEventsDao): UnlockEventsRepository =
        UnlockEventsRepositoryImpl(unlockEventsDao)

    @Provides
    @Singleton
    fun provideTimeRepository(): TimeRepository = TimeRepositoryImpl()
}