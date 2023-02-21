package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.domain.model.LockEvent
import com.sweak.unlockmaster.domain.repository.LockEventsRepository

class LockEventsRepositoryFake : LockEventsRepository {

    var lockEventsSinceTimeToBeReturned: List<LockEvent> = emptyList()

    override suspend fun addLockEvent(lockEventTimeInMillis: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getLockEventsSinceTime(sinceTimeInMillis: Long): List<LockEvent> =
        lockEventsSinceTimeToBeReturned
}