package com.sweak.unlockmaster.domain.repository

import com.sweak.unlockmaster.data.local.database.entities.UnlockLimit

interface UnlockLimitsRepository {
    suspend fun addUnlockLimit(limitApplianceDayTimeInMillis: Long, limitAmount: Int)
    suspend fun updateUnlockLimit(limitApplianceDayTimeInMillis: Long, limitAmount: Int)
    suspend fun getCurrentUnlockLimit(currentTimeInMillis: Long): UnlockLimit?
    suspend fun getUnlockLimitWithApplianceDay(limitApplianceDayTimeInMillis: Long): UnlockLimit?
}