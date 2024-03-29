package com.sweak.unlockmaster.domain.repository

import com.sweak.unlockmaster.domain.model.ScreenTimeLimit

interface ScreenTimeLimitsRepository {
    suspend fun addScreenTimeLimit(screenTimeLimit: ScreenTimeLimit)
    suspend fun updateScreenTimeLimit(screenTimeLimit: ScreenTimeLimit)
    suspend fun getScreenTimeLimitActiveAtTime(timeInMillis: Long): ScreenTimeLimit?
    suspend fun getScreenTimeLimitWithApplianceTime(
        limitApplianceTimeInMillis: Long
    ): ScreenTimeLimit?
}