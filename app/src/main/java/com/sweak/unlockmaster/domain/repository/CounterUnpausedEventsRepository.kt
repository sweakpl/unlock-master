package com.sweak.unlockmaster.domain.repository

import com.sweak.unlockmaster.domain.model.CounterUnpausedEvent

interface CounterUnpausedEventsRepository {
    suspend fun addCounterUnpausedEvent(counterUnpausedEventTimeInMillis: Long)

    suspend fun getCounterUnpausedEventsSinceTime(sinceTimeInMillis: Long): List<CounterUnpausedEvent>
}