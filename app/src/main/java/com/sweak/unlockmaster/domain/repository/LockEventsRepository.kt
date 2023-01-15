package com.sweak.unlockmaster.domain.repository

interface LockEventsRepository {
    suspend fun addLockEvent(lockEventTimeInMillis: Long)
}