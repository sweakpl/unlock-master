package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.CounterPausedEvent
import com.sweak.unlockmaster.domain.repository.CounterPausedEventsRepository

class CounterPausedEventsRepositoryFake : CounterPausedEventsRepository {

    var counterPausedEventsSinceTimeToBeReturned: List<CounterPausedEvent> = emptyList()

    override suspend fun addCounterPausedEvent(counterPausedEventTimeInMillis: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getCounterPausedEventsSinceTime(sinceTimeInMillis: Long): List<CounterPausedEvent> {
        return counterPausedEventsSinceTimeToBeReturned
    }
}