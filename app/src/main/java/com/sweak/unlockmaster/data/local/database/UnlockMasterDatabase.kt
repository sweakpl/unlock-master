package com.sweak.unlockmaster.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sweak.unlockmaster.data.local.database.dao.*
import com.sweak.unlockmaster.data.local.database.entities.*

@Database(
    entities = [
        UnlockEventEntity::class,
        LockEventEntity::class,
        ScreenOnEventEntity::class,
        UnlockLimitEntity::class,
        CounterPausedEventEntity::class,
        CounterUnpausedEventEntity::class
    ],
    version = 5
)
abstract class UnlockMasterDatabase : RoomDatabase() {
    abstract fun unlockEventsDao(): UnlockEventsDao
    abstract fun lockEventsDao(): LockEventsDao
    abstract fun screenOnEventsDao(): ScreenOnEventsDao
    abstract fun unlockLimitsDao(): UnlockLimitsDao
    abstract fun counterPausedEventsDao(): CounterPausedEventsDao
    abstract fun counterUnpausedEventsDao(): CounterUnpausedEventsDao
}