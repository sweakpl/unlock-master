package com.sweak.unlockmaster.di

import android.app.Application
import android.app.KeyguardManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import com.sweak.unlockmaster.data.local.database.UnlockMasterDatabase
import com.sweak.unlockmaster.data.local.database.dao.LockEventsDao
import com.sweak.unlockmaster.data.local.database.dao.UnlockEventsDao
import com.sweak.unlockmaster.data.local.database.dao.UnlockLimitsDao
import com.sweak.unlockmaster.data.repository.*
import com.sweak.unlockmaster.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun provideNotificationManager(app: Application): NotificationManagerCompat =
        NotificationManagerCompat.from(app)

    @Provides
    fun provideKeyguardManager(app: Application): KeyguardManager =
        app.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

    @Provides
    @Singleton
    fun provideUnlockMasterDatabase(app: Application): UnlockMasterDatabase =
        Room
            .databaseBuilder(
                app.applicationContext,
                UnlockMasterDatabase::class.java,
                "unlock_master_database"
            )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideUserSessionRepository(app: Application):UserSessionRepository =
        UserSessionRepositoryImpl(app)

    @Provides
    @Singleton
    fun provideUnlockEventsDao(database: UnlockMasterDatabase): UnlockEventsDao =
        database.unlockEventsDao()

    @Provides
    @Singleton
    fun provideLockEventsDao(database: UnlockMasterDatabase): LockEventsDao =
        database.lockEventsDao()

    @Provides
    @Singleton
    fun provideUnlockLimitsDao(database: UnlockMasterDatabase): UnlockLimitsDao =
        database.unlockLimitsDao()

    @Provides
    @Singleton
    fun provideUnlockEventsRepository(unlockEventsDao: UnlockEventsDao): UnlockEventsRepository =
        UnlockEventsRepositoryImpl(unlockEventsDao)

    @Provides
    @Singleton
    fun provideLockEventsRepository(lockEventsDao: LockEventsDao): LockEventsRepository =
        LockEventsRepositoryImpl(lockEventsDao)

    @Provides
    @Singleton
    fun provideUnlockLimitsRepository(unlockLimitsDao: UnlockLimitsDao): UnlockLimitsRepository =
        UnlockLimitsRepositoryImpl(unlockLimitsDao)

    @Provides
    @Singleton
    fun provideTimeRepository(): TimeRepository = TimeRepositoryImpl()
}