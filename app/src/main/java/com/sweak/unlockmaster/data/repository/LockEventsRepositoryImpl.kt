package com.sweak.unlockmaster.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.sweak.unlockmaster.data.local.database.dao.LockEventsDao
import com.sweak.unlockmaster.data.local.database.entities.LockEventEntity
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.LockEvent
import com.sweak.unlockmaster.domain.repository.LockEventsRepository

class LockEventsRepositoryImpl(
    private val lockEventsDao: LockEventsDao
) : LockEventsRepository {

    override suspend fun addLockEvent(lockEvent: LockEvent) {
        try {
            lockEventsDao.insert(
                LockEventEntity(timeInMillis = lockEvent.timeInMillis)
            )
        } catch (_: SQLiteConstraintException) { /* no-op */ }
    }

    override suspend fun getLockEventsSinceTime(sinceTimeInMillis: Long): List<LockEvent> =
        lockEventsDao.getAllLockEvents()
            .filter {
                it.timeInMillis >= sinceTimeInMillis
            }
            .map {
                LockEvent(lockTimeInMillis = it.timeInMillis)
            }
}