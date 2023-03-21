package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.data.local.database.dao.CounterPausedEventsDao
import com.sweak.unlockmaster.data.local.database.entities.CounterPausedEventEntity
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.CounterPausedEvent
import com.sweak.unlockmaster.domain.repository.CounterPausedEventsRepository

class CounterPausedEventsRepositoryImpl(
    private val counterPausedEventsDao: CounterPausedEventsDao
) : CounterPausedEventsRepository {

    override suspend fun addCounterPausedEvent(counterPausedEventTimeInMillis: Long) {
        counterPausedEventsDao.insert(
            CounterPausedEventEntity(timeInMillis = counterPausedEventTimeInMillis)
        )
    }

    override suspend fun getCounterPausedEventsSinceTime(
        sinceTimeInMillis: Long
    ): List<CounterPausedEvent> =
        counterPausedEventsDao.getCounterPausedEventsSinceTime(
            sinceTimeInMillis = sinceTimeInMillis
        ).map {
            CounterPausedEvent(counterPausedTimeInMillis = it.timeInMillis)
        }
}