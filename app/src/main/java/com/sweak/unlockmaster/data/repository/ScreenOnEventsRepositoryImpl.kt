package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.data.local.database.dao.ScreenOnEventsDao
import com.sweak.unlockmaster.data.local.database.entities.ScreenOnEventEntity
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.ScreenOnEvent
import com.sweak.unlockmaster.domain.repository.ScreenOnEventsRepository
import javax.inject.Inject

class ScreenOnEventsRepositoryImpl @Inject constructor(
    private val screenOnEventsDao: ScreenOnEventsDao
) : ScreenOnEventsRepository {

    override suspend fun addScreenOnEvent(screenOnEventTimeInMillis: Long) {
        screenOnEventsDao.insert(
            ScreenOnEventEntity(timeInMillis = screenOnEventTimeInMillis)
        )
    }

    override suspend fun getLatestScreenOnEvent(): ScreenOnEvent? =
        screenOnEventsDao.getLatestScreenOnEvent()?.let {
            ScreenOnEvent(
                screenOnTimeInMillis = it.timeInMillis
            )
        }

    override suspend fun getScreenOnEventsSinceTimeAndUntilTime(
        sinceTimeInMillis: Long,
        untilTimeInMillis: Long
    ): List<ScreenOnEvent> =
        screenOnEventsDao.getScreenOnEventsSinceTimeUntilTime(
            sinceTimeInMillis = sinceTimeInMillis,
            untilTimeInMillis = untilTimeInMillis
        ).map {
            ScreenOnEvent(screenOnTimeInMillis = it.timeInMillis)
        }
}