package com.sweak.unlockmaster.di

import android.app.Application
import android.content.ContentResolver
import com.sweak.unlockmaster.data.local.database.UnlockMasterDatabase
import com.sweak.unlockmaster.data.management.UnlockMasterBackupManagerImpl
import com.sweak.unlockmaster.domain.management.UnlockMasterBackupManager
import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import com.sweak.unlockmaster.domain.use_case.daily_wrap_up.ScheduleDailyWrapUpNotificationsUseCase
import com.sweak.unlockmaster.domain.use_case.screen_time_limits.AddOrUpdateScreenTimeLimitForTodayUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideUnlockMasterBackupManager(
        database: UnlockMasterDatabase,
        userSessionRepository: UserSessionRepository,
        timeRepository: TimeRepository,
        scheduleDailyWrapUpNotificationsUseCase: ScheduleDailyWrapUpNotificationsUseCase,
        addOrUpdateScreenTimeLimitForTodayUseCase: AddOrUpdateScreenTimeLimitForTodayUseCase
    ): UnlockMasterBackupManager =
        UnlockMasterBackupManagerImpl(
            database,
            userSessionRepository,
            timeRepository,
            scheduleDailyWrapUpNotificationsUseCase,
            addOrUpdateScreenTimeLimitForTodayUseCase
        )

    @Provides
    fun provideContentResolver(app: Application): ContentResolver = app.contentResolver
}