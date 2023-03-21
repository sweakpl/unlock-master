package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.CounterUnpausedEvent
import com.sweak.unlockmaster.domain.repository.CounterUnpausedEventsRepository

class CounterUnpausedEventsRepositoryFake : CounterUnpausedEventsRepository {

    var counterUnpausedEventsSinceTimeToBeReturned: List<CounterUnpausedEvent> = emptyList()

    override suspend fun addCounterUnpausedEvent(counterUnpausedEventTimeInMillis: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getCounterUnpausedEventsSinceTime(sinceTimeInMillis: Long): List<CounterUnpausedEvent> {
        return counterUnpausedEventsSinceTimeToBeReturned
    }
}