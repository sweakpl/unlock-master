package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.data.local.database.dao.UnlockLimitsDao
import com.sweak.unlockmaster.data.local.database.entities.UnlockLimitEntity
import com.sweak.unlockmaster.domain.model.UnlockLimit
import com.sweak.unlockmaster.domain.repository.UnlockLimitsRepository

class UnlockLimitsRepositoryImpl(
    private val unlockLimitsDao: UnlockLimitsDao
) : UnlockLimitsRepository {

    override suspend fun addUnlockLimit(unlockLimit: UnlockLimit) {
        unlockLimitsDao.insert(
            UnlockLimitEntity(
                limitApplianceDayTimeInMillis = unlockLimit.limitApplianceTimeInMillis,
                limitAmount = unlockLimit.limitAmount
            )
        )
    }

    override suspend fun updateUnlockLimit(unlockLimit: UnlockLimit) {
        unlockLimitsDao.update(
            UnlockLimitEntity(
                limitApplianceDayTimeInMillis = unlockLimit.limitApplianceTimeInMillis,
                limitAmount = unlockLimit.limitAmount
            )
        )
    }

    override suspend fun getUnlockLimitActiveAtTime(timeInMillis: Long): UnlockLimit? {
        val allUnlockLimits = unlockLimitsDao.getAllUnlockLimits()
        val applianceTime = allUnlockLimits
            .filter {
                it.limitApplianceDayTimeInMillis <= timeInMillis
            }
            .maxOf {
                it.limitApplianceDayTimeInMillis
            }

        return getUnlockLimitWithApplianceTime(applianceTime)
    }

    override suspend fun getUnlockLimitWithApplianceTime(
        limitApplianceTimeInMillis: Long
    ): UnlockLimit? =
        unlockLimitsDao.getAllUnlockLimits()
            .firstOrNull {
                it.limitApplianceDayTimeInMillis == limitApplianceTimeInMillis
            }?.let {
                UnlockLimit(
                    limitApplianceTimeInMillis = it.limitApplianceDayTimeInMillis,
                    limitAmount = it.limitAmount
                )
            }

    override suspend fun deleteUnlockLimitWithApplianceTime(limitApplianceTimeInMillis: Long) {
        getUnlockLimitWithApplianceTime(limitApplianceTimeInMillis)?.let {
            unlockLimitsDao.delete(
                UnlockLimitEntity(
                    limitApplianceDayTimeInMillis = it.limitApplianceTimeInMillis,
                    limitAmount = it.limitAmount
                )
            )
        }
    }
}