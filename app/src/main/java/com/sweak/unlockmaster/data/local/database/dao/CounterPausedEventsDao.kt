package com.sweak.unlockmaster.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sweak.unlockmaster.data.local.database.entities.CounterPausedEventEntity
import com.sweak.unlockmaster.data.local.database.entities.CounterUnpausedEventEntity
import com.sweak.unlockmaster.data.local.database.entities.UnlockEventEntity

@Dao
interface CounterPausedEventsDao {

    @Insert
    suspend fun insert(counterPausedEventEntity: CounterPausedEventEntity)

    @Query("SELECT * FROM counter_paused_event WHERE timeInMillis >= :sinceTimeInMillis")
    suspend fun getCounterPausedEventsSinceTime(
        sinceTimeInMillis: Long
    ): List<CounterPausedEventEntity>
}