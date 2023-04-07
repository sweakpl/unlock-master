package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.UnlockEvent
import com.sweak.unlockmaster.domain.repository.UnlockEventsRepository

class UnlockEventsRepositoryFake : UnlockEventsRepository {

    var unlockEventsSinceTimeToBeReturned: List<UnlockEvent> = emptyList()

    override suspend fun addUnlockEvent(unlockEventTimeInMillis: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getUnlockEventsCountSinceTime(sinceTimeInMillis: Long): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getUnlockEventsSinceTime(sinceTimeInMillis: Long): List<UnlockEvent> =
        unlockEventsSinceTimeToBeReturned

    override suspend fun getLatestUnlockEvent(): UnlockEvent? {
        TODO("Not yet implemented")
    }

    override suspend fun getFirstUnlockEvent(): UnlockEvent? {
        TODO("Not yet implemented")
    }
}