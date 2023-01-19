package com.sweak.unlockmaster.domain.repository

interface UserSessionRepository {
    suspend fun setIntroductionFinished()
    suspend fun isIntroductionFinished(): Boolean
}