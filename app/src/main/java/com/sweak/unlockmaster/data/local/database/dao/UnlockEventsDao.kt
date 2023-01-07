package com.sweak.unlockmaster.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sweak.unlockmaster.data.local.database.entities.UnlockEvent

@Dao
interface UnlockEventsDao {

    @Insert
    suspend fun insert(unlockEvent: UnlockEvent)

    @Query("SELECT COUNT(*) FROM unlock_event WHERE timeInMillis >= :sinceTimeInMillis")
    suspend fun getUnlockEventsCountSinceTime(sinceTimeInMillis: Long): Int
}