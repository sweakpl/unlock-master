package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.data.local.database.dao.ScreenOnEventsDao
import com.sweak.unlockmaster.data.local.database.entities.ScreenOnEventEntity
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.ScreenOnEvent
import com.sweak.unlockmaster.domain.repository.ScreenOnEventsRepository
import javax.inject.Inject

class ScreenOnEventsRepositoryImpl @Inject constructor(
    private val screenOnEventsDao: ScreenOnEventsDao
) : ScreenOnEventsRepository {

    override suspend fun addScreenOnEvent(screenOnEvent: ScreenOnEvent) {
        screenOnEventsDao.insert(
            ScreenOnEventEntity(timeInMillis = screenOnEvent.timeInMillis)
        )
    }

    override suspend fun getLatestScreenOnEvent(): ScreenOnEvent? =
        screenOnEventsDao.getAllScreenOnEvents()
            .maxByOrNull {
                it.timeInMillis
            }?.let {
                ScreenOnEvent(screenOnTimeInMillis = it.timeInMillis)
            }

    override suspend fun getScreenOnEventsSinceTimeAndUntilTime(
        sinceTimeInMillis: Long,
        untilTimeInMillis: Long
    ): List<ScreenOnEvent> =
        screenOnEventsDao.getAllScreenOnEvents()
            .filter {
                it.timeInMillis in sinceTimeInMillis until untilTimeInMillis
            }.map {
                ScreenOnEvent(screenOnTimeInMillis = it.timeInMillis)
            }
}