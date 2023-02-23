package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.data.local.database.dao.CounterUnpausedEventsDao
import com.sweak.unlockmaster.data.local.database.entities.CounterUnpausedEventEntity
import com.sweak.unlockmaster.domain.model.CounterUnpausedEvent
import com.sweak.unlockmaster.domain.repository.CounterUnpausedEventsRepository

class CounterUnpausedEventsRepositoryImpl(
    private val counterUnpausedEventsDao: CounterUnpausedEventsDao
) : CounterUnpausedEventsRepository {

    override suspend fun addCounterUnpausedEvent(counterUnpausedEventTimeInMillis: Long) {
        counterUnpausedEventsDao.insert(
            CounterUnpausedEventEntity(timeInMillis = counterUnpausedEventTimeInMillis)
        )
    }

    override suspend fun getCounterUnpausedEventsSinceTime(
        sinceTimeInMillis: Long
    ): List<CounterUnpausedEvent> =
        counterUnpausedEventsDao.getCounterUnpausedEventsSinceTime(
            sinceTimeInMillis = sinceTimeInMillis
        ).map {
            CounterUnpausedEvent(counterUnpausedTimeInMillis = it.timeInMillis)
        }
}