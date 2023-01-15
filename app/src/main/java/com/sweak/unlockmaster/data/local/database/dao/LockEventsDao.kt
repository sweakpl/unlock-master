package com.sweak.unlockmaster.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.sweak.unlockmaster.data.local.database.entities.LockEvent

@Dao
interface LockEventsDao {

    @Insert
    suspend fun insert(lockEvent: LockEvent)
}