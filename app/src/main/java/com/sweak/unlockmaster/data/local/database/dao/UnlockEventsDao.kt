package com.sweak.unlockmaster.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sweak.unlockmaster.data.local.database.entities.UnlockEventEntity

@Dao
interface UnlockEventsDao {

    @Insert
    suspend fun insert(unlockEventEntity: UnlockEventEntity)

    @Query("SELECT COUNT(*) FROM unlock_event WHERE timeInMillis >= :sinceTimeInMillis")
    suspend fun getUnlockEventsCountSinceTime(sinceTimeInMillis: Long): Int

    @Query("SELECT * FROM unlock_event WHERE timeInMillis >= :sinceTimeInMillis")
    suspend fun getUnlockEventsSinceTime(sinceTimeInMillis: Long): List<UnlockEventEntity>

    @Query("SELECT * FROM unlock_event ORDER BY timeInMillis DESC LIMIT 1")
    suspend fun getLatestUnlockEvent(): UnlockEventEntity?
}