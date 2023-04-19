package com.sweak.unlockmaster.domain.repository

import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.LockEvent

interface LockEventsRepository {
    suspend fun addLockEvent(lockEvent: LockEvent)

    suspend fun getLockEventsSinceTime(sinceTimeInMillis: Long): List<LockEvent>
}