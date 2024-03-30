package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.data.local.database.dao.ScreenTimeLimitsDao
import com.sweak.unlockmaster.data.local.database.entities.ScreenTimeLimitEntity
import com.sweak.unlockmaster.domain.model.ScreenTimeLimit
import com.sweak.unlockmaster.domain.repository.ScreenTimeLimitsRepository

class ScreenTimeLimitsRepositoryImpl(
    private val screenTimeLimitsDao: ScreenTimeLimitsDao
) : ScreenTimeLimitsRepository {

    override suspend fun addScreenTimeLimit(screenTimeLimit: ScreenTimeLimit) {
        screenTimeLimitsDao.insert(
            ScreenTimeLimitEntity(
                limitApplianceDayTimeInMillis = screenTimeLimit.limitApplianceTimeInMillis,
                limitAmountMinutes = screenTimeLimit.limitAmountMinutes
            )
        )
    }

    override suspend fun updateScreenTimeLimit(screenTimeLimit: ScreenTimeLimit) {
        screenTimeLimitsDao.update(
            ScreenTimeLimitEntity(
                limitApplianceDayTimeInMillis = screenTimeLimit.limitApplianceTimeInMillis,
                limitAmountMinutes = screenTimeLimit.limitAmountMinutes
            )
        )
    }

    override suspend fun getScreenTimeLimitActiveAtTime(timeInMillis: Long): ScreenTimeLimit? {
        val allScreenTimeLimits = screenTimeLimitsDao.getAllScreenTimeLimits()
        val applianceTime = allScreenTimeLimits
            .filter {
                it.limitApplianceDayTimeInMillis <= timeInMillis
            }
            .maxOfOrNull {
                it.limitApplianceDayTimeInMillis
            }

        return applianceTime?.let { getScreenTimeLimitWithApplianceTime(it) }
    }

    override suspend fun getScreenTimeLimitWithApplianceTime(
        limitApplianceTimeInMillis: Long
    ): ScreenTimeLimit? =
        screenTimeLimitsDao.getAllScreenTimeLimits()
            .firstOrNull {
                it.limitApplianceDayTimeInMillis == limitApplianceTimeInMillis
            }?.let {
                ScreenTimeLimit(
                    limitApplianceTimeInMillis = it.limitApplianceDayTimeInMillis,
                    limitAmountMinutes = it.limitAmountMinutes
                )
            }

    override suspend fun deleteScreenTimeLimitWithApplianceTime(limitApplianceTimeInMillis: Long) {
        getScreenTimeLimitWithApplianceTime(limitApplianceTimeInMillis)?.let {
            screenTimeLimitsDao.delete(
                ScreenTimeLimitEntity(
                    limitApplianceDayTimeInMillis = it.limitApplianceTimeInMillis,
                    limitAmountMinutes = it.limitAmountMinutes
                )
            )
        }
    }
}