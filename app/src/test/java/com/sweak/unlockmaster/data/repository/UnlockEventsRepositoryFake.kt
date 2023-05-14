package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.UnlockEvent
import com.sweak.unlockmaster.domain.repository.UnlockEventsRepository

class UnlockEventsRepositoryFake : UnlockEventsRepository {

    var unlockEventsSinceTimeToBeReturned: List<UnlockEvent> = emptyList()
    var firstUnlockEventToBeReturned: UnlockEvent? = null

    override suspend fun addUnlockEvent(unlockEvent: UnlockEvent) {
        TODO("Not yet implemented")
    }

    override suspend fun getUnlockEventsSinceTime(sinceTimeInMillis: Long): List<UnlockEvent> =
        unlockEventsSinceTimeToBeReturned

    override suspend fun getUnlockEventsSinceTimeAndUntilTime(
        sinceTimeInMillis: Long,
        untilTimeInMillis: Long
    ): List<UnlockEvent> {
        TODO("Not yet implemented")
    }

    override suspend fun getLatestUnlockEvent(): UnlockEvent? {
        TODO("Not yet implemented")
    }

    override suspend fun getFirstUnlockEvent(): UnlockEvent? = firstUnlockEventToBeReturned
}