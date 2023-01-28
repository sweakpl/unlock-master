package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.data.local.database.dao.UnlockLimitsDao
import com.sweak.unlockmaster.data.local.database.entities.UnlockLimit
import com.sweak.unlockmaster.domain.repository.UnlockLimitsRepository

class UnlockLimitsRepositoryImpl(
    private val unlockLimitsDao: UnlockLimitsDao
) : UnlockLimitsRepository {

    override suspend fun addUnlockLimit(limitApplianceDayTimeInMillis: Long, limitAmount: Int) {
        unlockLimitsDao.insert(
            UnlockLimit(
                limitApplianceDayTimeInMillis = limitApplianceDayTimeInMillis,
                limitAmount = limitAmount
            )
        )
    }

    override suspend fun updateUnlockLimit(limitApplianceDayTimeInMillis: Long, limitAmount: Int) {
        unlockLimitsDao.update(
            UnlockLimit(
                limitApplianceDayTimeInMillis = limitApplianceDayTimeInMillis,
                limitAmount = limitAmount
            )
        )
    }

    override suspend fun getCurrentUnlockLimit(currentTimeInMillis: Long): UnlockLimit? =
        unlockLimitsDao.getCurrentUnlockLimit(currentTimeInMillis = currentTimeInMillis)

    override suspend fun getUnlockLimitWithApplianceDay(
        limitApplianceDayTimeInMillis: Long
    ): UnlockLimit? =
        unlockLimitsDao.getUnlockLimitWithApplianceDay(
            limitApplianceDayTimeInMillis = limitApplianceDayTimeInMillis
        )

    override suspend fun deleteUnlockLimitWithApplianceDay(limitApplianceDayTimeInMillis: Long) {
        unlockLimitsDao.getUnlockLimitWithApplianceDay(
            limitApplianceDayTimeInMillis = limitApplianceDayTimeInMillis
        )?.let {
            unlockLimitsDao.delete(it)
        }
    }
}