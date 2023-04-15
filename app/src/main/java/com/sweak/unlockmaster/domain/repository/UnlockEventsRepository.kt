package com.sweak.unlockmaster.domain.repository

import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.UnlockEvent

interface UnlockEventsRepository {
    suspend fun addUnlockEvent(unlockEventTimeInMillis: Long)
    suspend fun getUnlockEventsSinceTime(sinceTimeInMillis: Long): List<UnlockEvent>

    suspend fun getUnlockEventsSinceTimeAndUntilTime(
        sinceTimeInMillis: Long,
        untilTimeInMillis: Long
    ): List<UnlockEvent>
    suspend fun getLatestUnlockEvent(): UnlockEvent?
    suspend fun getFirstUnlockEvent(): UnlockEvent?
}