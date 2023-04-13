package com.sweak.unlockmaster.domain.repository

import com.sweak.unlockmaster.data.local.database.entities.UnlockLimitEntity

interface UnlockLimitsRepository {
    suspend fun addUnlockLimit(limitApplianceDayTimeInMillis: Long, limitAmount: Int)
    suspend fun updateUnlockLimit(limitApplianceDayTimeInMillis: Long, limitAmount: Int)
    suspend fun getUnlockLimitFromTime(currentTimeInMillis: Long): UnlockLimitEntity?
    suspend fun getUnlockLimitWithApplianceDay(limitApplianceDayTimeInMillis: Long): UnlockLimitEntity?
    suspend fun deleteUnlockLimitWithApplianceDay(limitApplianceDayTimeInMillis: Long)
}