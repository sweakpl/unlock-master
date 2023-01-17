package com.sweak.unlockmaster.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sweak.unlockmaster.data.local.database.dao.LockEventsDao
import com.sweak.unlockmaster.data.local.database.dao.UnlockEventsDao
import com.sweak.unlockmaster.data.local.database.dao.UnlockLimitsDao
import com.sweak.unlockmaster.data.local.database.entities.LockEvent
import com.sweak.unlockmaster.data.local.database.entities.UnlockEvent
import com.sweak.unlockmaster.data.local.database.entities.UnlockLimit

@Database(
    entities = [UnlockEvent::class, LockEvent::class, UnlockLimit::class],
    version = 3
)
abstract class UnlockMasterDatabase : RoomDatabase() {
    abstract fun unlockEventsDao(): UnlockEventsDao
    abstract fun lockEventsDao(): LockEventsDao
    abstract fun unlockLimitsDao(): UnlockLimitsDao
}