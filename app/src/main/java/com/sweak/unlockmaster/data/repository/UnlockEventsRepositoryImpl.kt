package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.data.local.database.dao.UnlockEventsDao
import com.sweak.unlockmaster.data.local.database.entities.UnlockEvent
import com.sweak.unlockmaster.domain.repository.UnlockEventsRepository

class UnlockEventsRepositoryImpl(
    private val unlockEventsDao: UnlockEventsDao
) : UnlockEventsRepository {

    override suspend fun addUnlockEvent(unlockEventTimeInMillis: Long) {
        unlockEventsDao.insert(
            UnlockEvent(timeInMillis = unlockEventTimeInMillis)
        )
    }

    override suspend fun getUnlockEventsCountSinceTime(sinceTimeInMillis: Long): Int =
        unlockEventsDao.getUnlockEventsCountSinceTime(sinceTimeInMillis = sinceTimeInMillis)
}