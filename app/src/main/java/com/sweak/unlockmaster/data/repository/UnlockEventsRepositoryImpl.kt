package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.data.local.database.dao.UnlockEventsDao
import com.sweak.unlockmaster.data.local.database.entities.UnlockEventEntity
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.UnlockEvent
import com.sweak.unlockmaster.domain.repository.UnlockEventsRepository

class UnlockEventsRepositoryImpl(
    private val unlockEventsDao: UnlockEventsDao
) : UnlockEventsRepository {

    override suspend fun addUnlockEvent(unlockEvent: UnlockEvent) {
        unlockEventsDao.insert(
            UnlockEventEntity(timeInMillis = unlockEvent.timeInMillis)
        )
    }

    override suspend fun getUnlockEventsSinceTime(sinceTimeInMillis: Long): List<UnlockEvent> =
        unlockEventsDao.getAllUnlockEvents()
            .filter {
                it.timeInMillis >= sinceTimeInMillis
            }
            .map {
                UnlockEvent(unlockTimeInMillis = it.timeInMillis)
            }

    override suspend fun getUnlockEventsSinceTimeAndUntilTime(
        sinceTimeInMillis: Long,
        untilTimeInMillis: Long
    ): List<UnlockEvent> =
        unlockEventsDao.getAllUnlockEvents()
            .filter {
                it.timeInMillis in sinceTimeInMillis until untilTimeInMillis
            }.map {
                UnlockEvent(unlockTimeInMillis = it.timeInMillis)
            }

    override suspend fun getLatestUnlockEvent(): UnlockEvent? =
        unlockEventsDao.getAllUnlockEvents()
            .maxByOrNull {
                it.timeInMillis
            }?.let {
                UnlockEvent(
                    unlockTimeInMillis = it.timeInMillis
                )
            }

    override suspend fun getFirstUnlockEvent(): UnlockEvent? =
        unlockEventsDao.getAllUnlockEvents()
            .minByOrNull {
                it.timeInMillis
            }?.let {
                UnlockEvent(
                    unlockTimeInMillis = it.timeInMillis
                )
            }
}