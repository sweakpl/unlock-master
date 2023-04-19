package com.sweak.unlockmaster.domain.repository

import com.sweak.unlockmaster.domain.model.UnlockLimit

interface UnlockLimitsRepository {
    suspend fun addUnlockLimit(unlockLimit: UnlockLimit)
    suspend fun updateUnlockLimit(unlockLimit: UnlockLimit)
    suspend fun getUnlockLimitActiveAtTime(timeInMillis: Long): UnlockLimit?
    suspend fun getUnlockLimitWithApplianceTime(limitApplianceTimeInMillis: Long): UnlockLimit?
    suspend fun deleteUnlockLimitWithApplianceTime(limitApplianceTimeInMillis: Long)
}