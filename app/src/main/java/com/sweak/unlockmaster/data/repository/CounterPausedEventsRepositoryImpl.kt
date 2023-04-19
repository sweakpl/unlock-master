package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.data.local.database.dao.CounterPausedEventsDao
import com.sweak.unlockmaster.data.local.database.entities.CounterPausedEventEntity
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.CounterPausedEvent
import com.sweak.unlockmaster.domain.repository.CounterPausedEventsRepository

class CounterPausedEventsRepositoryImpl(
    private val counterPausedEventsDao: CounterPausedEventsDao
) : CounterPausedEventsRepository {

    override suspend fun addCounterPausedEvent(counterPausedEvent: CounterPausedEvent) {
        counterPausedEventsDao.insert(
            CounterPausedEventEntity(timeInMillis = counterPausedEvent.timeInMillis)
        )
    }

    override suspend fun getCounterPausedEventsSinceTime(
        sinceTimeInMillis: Long
    ): List<CounterPausedEvent> =
        counterPausedEventsDao.getAllCounterPausedEvents()
            .filter {
                it.timeInMillis >= sinceTimeInMillis
            }
            .map {
                CounterPausedEvent(counterPausedTimeInMillis = it.timeInMillis)
            }
}