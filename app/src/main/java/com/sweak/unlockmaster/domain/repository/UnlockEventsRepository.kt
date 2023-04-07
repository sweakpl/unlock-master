package com.sweak.unlockmaster.domain.repository

import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.UnlockEvent

interface UnlockEventsRepository {
    suspend fun addUnlockEvent(unlockEventTimeInMillis: Long)
    suspend fun getUnlockEventsCountSinceTime(sinceTimeInMillis: Long): Int
    suspend fun getUnlockEventsSinceTime(sinceTimeInMillis: Long): List<UnlockEvent>
    suspend fun getLatestUnlockEvent(): UnlockEvent?
    suspend fun getFirstUnlockEvent(): UnlockEvent?
}