package com.sweak.unlockmaster.domain.repository

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
}