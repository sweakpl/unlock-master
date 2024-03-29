package com.sweak.unlockmaster.di

import android.app.AlarmManager
import android.app.Application
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import com.sweak.unlockmaster.data.local.database.UnlockMasterDatabase
import com.sweak.unlockmaster.data.local.database.dao.*
import com.sweak.unlockmaster.data.management.UnlockMasterAlarmManagerImpl
import com.sweak.unlockmaster.data.repository.*
import com.sweak.unlockmaster.domain.management.UnlockMasterAlarmManager
import com.sweak.unlockmaster.domain.repository.*
import com.sweak.unlockmaster.presentation.background_work.local_receivers.DailyWrapUpAlarmReceiver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun provideNotificationManager(app: Application): NotificationManagerCompat =
        NotificationManagerCompat.from(app)

    @Provides
    fun providePowerManager(app: Application): PowerManager =
        app.getSystemService(Context.POWER_SERVICE) as PowerManager

    @Provides
    @Named("PackageName")
    fun providePackageName(app: Application): String = app.packageName

    @Provides
    fun provideAlarmManger(app: Application): AlarmManager =
        app.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @Provides
    fun provideUnlockMasterAlarmManager(
        alarmManager: AlarmManager,
        timeRepository: TimeRepository,
        @Named("DailyWrapUpAlarmIntent") dailyWrapUpAlarmIntent: Intent,
        app: Application
    ): UnlockMasterAlarmManager =
        UnlockMasterAlarmManagerImpl(alarmManager, timeRepository, dailyWrapUpAlarmIntent, app)

    @Provides
    @Named("DailyWrapUpAlarmIntent")
    fun provideDailyWrapUpAlarmIntent(app: Application): Intent =
        Intent(app, DailyWrapUpAlarmReceiver::class.java)

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
            .fallbackToDestructiveMigration() // TODO
            .build()

    @Provides
    @Singleton
    fun provideUserSessionRepository(app: Application): UserSessionRepository =
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
    fun provideScreenOnEventsDao(database: UnlockMasterDatabase): ScreenOnEventsDao =
        database.screenOnEventsDao()

    @Provides
    @Singleton
    fun provideUnlockLimitsDao(database: UnlockMasterDatabase): UnlockLimitsDao =
        database.unlockLimitsDao()

    @Provides
    @Singleton
    fun provideScreenTimeLimitsDao(database: UnlockMasterDatabase): ScreenTimeLimitsDao =
        database.screenTimeLimitsDao()

    @Provides
    @Singleton
    fun provideCounterPausedDao(database: UnlockMasterDatabase): CounterPausedEventsDao =
        database.counterPausedEventsDao()

    @Provides
    @Singleton
    fun provideCounterUnpausedDao(database: UnlockMasterDatabase): CounterUnpausedEventsDao =
        database.counterUnpausedEventsDao()

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
    fun provideScreenOnEventsRepository(screenOnEventsDao: ScreenOnEventsDao): ScreenOnEventsRepository =
        ScreenOnEventsRepositoryImpl(screenOnEventsDao)

    @Provides
    @Singleton
    fun provideUnlockLimitsRepository(unlockLimitsDao: UnlockLimitsDao): UnlockLimitsRepository =
        UnlockLimitsRepositoryImpl(unlockLimitsDao)

    @Provides
    @Singleton
    fun provideScreenTimeLimitsRepository(
        screenTimeLimitsDao: ScreenTimeLimitsDao
    ): ScreenTimeLimitsRepository = ScreenTimeLimitsRepositoryImpl(screenTimeLimitsDao)

    @Provides
    @Singleton
    fun provideCounterPausedRepository(
        counterPausedEventsDao: CounterPausedEventsDao
    ): CounterPausedEventsRepository =
        CounterPausedEventsRepositoryImpl(counterPausedEventsDao)

    @Provides
    @Singleton
    fun provideCounterUnpausedRepository(
        counterUnpausedEventsDao: CounterUnpausedEventsDao
    ): CounterUnpausedEventsRepository =
        CounterUnpausedEventsRepositoryImpl(counterUnpausedEventsDao)

    @Provides
    @Singleton
    fun provideTimeRepository(): TimeRepository = TimeRepositoryImpl()
}