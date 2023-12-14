package com.sweak.unlockmaster.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sweak.unlockmaster.data.local.database.entities.CounterPausedEventEntity

@Dao
interface CounterPausedEventsDao {

    @Insert
    suspend fun insert(counterPausedEventEntity: CounterPausedEventEntity)

    @Insert
    suspend fun insertAll(counterPausedEventsEntities: List<CounterPausedEventEntity>)

    @Query("SELECT * FROM counter_paused_event")
    suspend fun getAllCounterPausedEvents(): List<CounterPausedEventEntity>
}