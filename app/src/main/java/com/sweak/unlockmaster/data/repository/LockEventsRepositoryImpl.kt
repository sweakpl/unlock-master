package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.data.local.database.dao.LockEventsDao
import com.sweak.unlockmaster.data.local.database.entities.LockEvent
import com.sweak.unlockmaster.domain.repository.LockEventsRepository

class LockEventsRepositoryImpl(
    private val lockEventsDao: LockEventsDao
) : LockEventsRepository {

    override suspend fun addLockEvent(lockEventTimeInMillis: Long) {
        lockEventsDao.insert(
            LockEvent(timeInMillis = lockEventTimeInMillis)
        )
    }
}