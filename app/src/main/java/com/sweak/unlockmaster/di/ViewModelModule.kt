package com.sweak.unlockmaster.di

import android.app.Application
import com.sweak.unlockmaster.data.local.database.UnlockMasterDatabase
import com.sweak.unlockmaster.data.management.UnlockMasterBackupManagerImpl
import com.sweak.unlockmaster.domain.management.UnlockMasterBackupManager
import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
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
        timeRepository: TimeRepository
    ): UnlockMasterBackupManager =
        UnlockMasterBackupManagerImpl(database, userSessionRepository, timeRepository)

    @Provides
    fun provideContentResolver(app: Application) = app.contentResolver
}