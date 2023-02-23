package com.sweak.unlockmaster.domain.repository

import com.sweak.unlockmaster.domain.model.CounterPausedEvent

interface CounterPausedEventsRepository {
    suspend fun addCounterPausedEvent(counterPausedEventTimeInMillis: Long)

    suspend fun getCounterPausedEventsSinceTime(sinceTimeInMillis: Long): List<CounterPausedEvent>
}