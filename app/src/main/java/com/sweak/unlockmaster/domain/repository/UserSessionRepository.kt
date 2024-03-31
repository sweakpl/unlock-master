package com.sweak.unlockmaster.domain.repository

import com.sweak.unlockmaster.domain.model.ScreenTimeLimitWarningState
import com.sweak.unlockmaster.domain.model.UiThemeMode
import kotlinx.coroutines.flow.Flow

interface UserSessionRepository {
    suspend fun setIntroductionFinished()
    suspend fun isIntroductionFinished(): Boolean
    suspend fun setUnlockCounterPaused(isPaused: Boolean)
    suspend fun isUnlockCounterPaused(): Boolean
    suspend fun setMobilizingNotificationsFrequencyPercentage(percentage: Int)
    suspend fun getMobilizingNotificationsFrequencyPercentage(): Int
    suspend fun setDailyWrapUpNotificationsTimeInMinutesAfterMidnight(minutes: Int)
    suspend fun getDailyWrapUpNotificationsTimeInMinutesAfterMidnight(): Int
    suspend fun setUnlockMasterServiceProperlyClosed(wasProperlyClosed: Boolean)
    suspend fun wasUnlockMasterServiceProperlyClosed(): Boolean
    suspend fun setShouldShowUnlockMasterBlockedWarning(shouldShowWarning: Boolean)
    suspend fun shouldShowUnlockMasterBlockedWarning(): Boolean
    suspend fun setUiThemeMode(uiThemeMode: UiThemeMode)
    fun getUiThemeModeFlow(): Flow<UiThemeMode>
    suspend fun setOverUnlockLimitMobilizingNotificationsEnabled(areEnabled: Boolean)
    suspend fun areOverUnlockLimitMobilizingNotificationsEnabled(): Boolean
    suspend fun setScreenTimeLimitEnabled(isEnabled: Boolean)
    suspend fun isScreenTimeLimitEnabled(): Boolean
    suspend fun setScreenTimeLimitWarningState(state: ScreenTimeLimitWarningState)
    suspend fun getScreenTimeLimitWarningState(): ScreenTimeLimitWarningState
}