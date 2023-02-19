package com.sweak.unlockmaster.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sweak.unlockmaster.data.local.database.entities.LockEventEntity
import com.sweak.unlockmaster.data.local.database.entities.UnlockEventEntity

@Dao
interface LockEventsDao {

    @Insert
    suspend fun insert(lockEventEntity: LockEventEntity)

    @Query("SELECT * FROM lock_event WHERE timeInMillis >= :sinceTimeInMillis")
    suspend fun getLockEventsSinceTime(sinceTimeInMillis: Long): List<UnlockEventEntity>
}