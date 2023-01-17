package com.sweak.unlockmaster.domain.repository

interface UnlockLimitsRepository {
    suspend fun addUnlockLimit(limitApplianceDayTimeInMillis: Long, limitAmount: Int)
    suspend fun updateUnlockLimit(limitApplianceDayTimeInMillis: Long, limitAmount: Int)
    suspend fun getCurrentUnlockLimit(currentTimeInMillis: Long): Int?
}