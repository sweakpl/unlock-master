package com.sweak.unlockmaster.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sweak.unlockmaster.data.local.database.entities.LockEventEntity

@Dao
interface LockEventsDao {

    @Insert
    suspend fun insert(lockEventEntity: LockEventEntity)

    @Insert
    suspend fun insertAll(lockEventsEntities: List<LockEventEntity>)

    @Query("SELECT * FROM lock_event")
    suspend fun getAllLockEvents(): List<LockEventEntity>
}