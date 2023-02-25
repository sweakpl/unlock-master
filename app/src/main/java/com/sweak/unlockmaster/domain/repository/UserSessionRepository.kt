package com.sweak.unlockmaster.domain.repository

interface UserSessionRepository {
    suspend fun setIntroductionFinished()
    suspend fun isIntroductionFinished(): Boolean
    suspend fun setUnlockCounterPaused(isPaused: Boolean)
    suspend fun isUnlockCounterPaused(): Boolean
}