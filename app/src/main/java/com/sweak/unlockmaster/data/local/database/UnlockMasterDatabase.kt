package com.sweak.unlockmaster.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sweak.unlockmaster.data.local.database.dao.UnlockEventsDao
import com.sweak.unlockmaster.data.local.database.entities.UnlockEvent

@Database(
    entities = [UnlockEvent::class],
    version = 1
)
abstract class UnlockMasterDatabase : RoomDatabase() {
    abstract fun unlockEventsDao(): UnlockEventsDao
}