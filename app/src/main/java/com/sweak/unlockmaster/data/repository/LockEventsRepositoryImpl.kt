package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.data.local.database.dao.LockEventsDao
import com.sweak.unlockmaster.data.local.database.entities.LockEventEntity
import com.sweak.unlockmaster.domain.model.LockEvent
import com.sweak.unlockmaster.domain.repository.LockEventsRepository

class LockEventsRepositoryImpl(
    private val lockEventsDao: LockEventsDao
) : LockEventsRepository {

    override suspend fun addLockEvent(lockEventTimeInMillis: Long) {
        lockEventsDao.insert(
            LockEventEntity(timeInMillis = lockEventTimeInMillis)
        )
    }

    override suspend fun getLockEventsSinceTime(sinceTimeInMillis: Long): List<LockEvent> =
        lockEventsDao.getLockEventsSinceTime(sinceTimeInMillis = sinceTimeInMillis).map {
            LockEvent(lockTimeInMillis = it.timeInMillis)
        }
}