package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.data.local.database.dao.CounterUnpausedEventsDao
import com.sweak.unlockmaster.data.local.database.entities.CounterUnpausedEventEntity
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.CounterUnpausedEvent
import com.sweak.unlockmaster.domain.repository.CounterUnpausedEventsRepository

class CounterUnpausedEventsRepositoryImpl(
    private val counterUnpausedEventsDao: CounterUnpausedEventsDao
) : CounterUnpausedEventsRepository {

    override suspend fun addCounterUnpausedEvent(counterUnpausedEvent: CounterUnpausedEvent) {
        counterUnpausedEventsDao.insert(
            CounterUnpausedEventEntity(timeInMillis = counterUnpausedEvent.timeInMillis)
        )
    }

    override suspend fun getCounterUnpausedEventsSinceTime(
        sinceTimeInMillis: Long
    ): List<CounterUnpausedEvent> =
        counterUnpausedEventsDao.getAllCounterUnpausedEvents()
            .filter {
                it.timeInMillis >= sinceTimeInMillis
            }
            .map {
                CounterUnpausedEvent(counterUnpausedTimeInMillis = it.timeInMillis)
            }
}