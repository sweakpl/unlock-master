package com.sweak.unlockmaster.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sweak.unlockmaster.data.local.database.entities.CounterUnpausedEventEntity

@Dao
interface CounterUnpausedEventsDao {

    @Insert
    suspend fun insert(counterUnpausedEventEntity: CounterUnpausedEventEntity)

    @Insert
    suspend fun insertAll(counterUnpausedEventsEntities: List<CounterUnpausedEventEntity>)

    @Query("SELECT * FROM counter_unpaused_event")
    suspend fun getAllCounterUnpausedEvents(): List<CounterUnpausedEventEntity>
}