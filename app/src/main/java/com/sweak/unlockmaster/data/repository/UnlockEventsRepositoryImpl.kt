package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.data.local.database.dao.UnlockEventsDao
import com.sweak.unlockmaster.data.local.database.entities.UnlockEventEntity
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.UnlockEvent
import com.sweak.unlockmaster.domain.repository.UnlockEventsRepository

class UnlockEventsRepositoryImpl(
    private val unlockEventsDao: UnlockEventsDao
) : UnlockEventsRepository {

    override suspend fun addUnlockEvent(unlockEventTimeInMillis: Long) {
        unlockEventsDao.insert(
            UnlockEventEntity(timeInMillis = unlockEventTimeInMillis)
        )
    }

    override suspend fun getUnlockEventsCountSinceTime(sinceTimeInMillis: Long): Int =
        unlockEventsDao.getUnlockEventsCountSinceTime(sinceTimeInMillis = sinceTimeInMillis)

    override suspend fun getUnlockEventsSinceTime(sinceTimeInMillis: Long): List<UnlockEvent> =
        unlockEventsDao.getUnlockEventsSinceTime(sinceTimeInMillis = sinceTimeInMillis).map {
            UnlockEvent(unlockTimeInMillis = it.timeInMillis)
        }

    override suspend fun getLatestUnlockEvent(): UnlockEvent? =
        unlockEventsDao.getLatestUnlockEvent()?.let {
            UnlockEvent(
                unlockTimeInMillis = it.timeInMillis
            )
        }
}