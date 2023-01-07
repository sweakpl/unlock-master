package com.sweak.unlockmaster.domain.repository

interface UnlockEventsRepository {
    suspend fun addUnlockEvent(unlockEventTimeInMillis: Long)
    suspend fun getUnlockEventsCountSinceTime(sinceTimeInMillis: Long): Int
}