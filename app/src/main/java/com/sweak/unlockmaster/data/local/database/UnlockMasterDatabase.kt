package com.sweak.unlockmaster.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sweak.unlockmaster.data.local.database.dao.LockEventsDao
import com.sweak.unlockmaster.data.local.database.dao.UnlockEventsDao
import com.sweak.unlockmaster.data.local.database.dao.UnlockLimitsDao
import com.sweak.unlockmaster.data.local.database.entities.LockEventEntity
import com.sweak.unlockmaster.data.local.database.entities.UnlockEventEntity
import com.sweak.unlockmaster.data.local.database.entities.UnlockLimitEntity

@Database(
    entities = [UnlockEventEntity::class, LockEventEntity::class, UnlockLimitEntity::class],
    version = 3
)
abstract class UnlockMasterDatabase : RoomDatabase() {
    abstract fun unlockEventsDao(): UnlockEventsDao
    abstract fun lockEventsDao(): LockEventsDao
    abstract fun unlockLimitsDao(): UnlockLimitsDao
}