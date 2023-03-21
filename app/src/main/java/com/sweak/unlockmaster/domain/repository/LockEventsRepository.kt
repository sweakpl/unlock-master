package com.sweak.unlockmaster.domain.repository

import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.LockEvent

interface LockEventsRepository {
    suspend fun addLockEvent(lockEventTimeInMillis: Long)

    suspend fun getLockEventsSinceTime(sinceTimeInMillis: Long): List<LockEvent>
}