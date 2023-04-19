package com.sweak.unlockmaster.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sweak.unlockmaster.data.local.database.entities.UnlockEventEntity

@Dao
interface UnlockEventsDao {

    @Insert
    suspend fun insert(unlockEventEntity: UnlockEventEntity)

    @Query("SELECT * FROM unlock_event")
    suspend fun getAllUnlockEvents(): List<UnlockEventEntity>
}