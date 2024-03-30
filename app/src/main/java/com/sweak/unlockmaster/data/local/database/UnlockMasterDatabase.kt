package com.sweak.unlockmaster.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sweak.unlockmaster.data.local.database.dao.*
import com.sweak.unlockmaster.data.local.database.entities.*

@Database(
    entities = [
        UnlockEventEntity::class,
        LockEventEntity::class,
        ScreenOnEventEntity::class,
        UnlockLimitEntity::class,
        ScreenTimeLimitEntity::class,
        CounterPausedEventEntity::class,
        CounterUnpausedEventEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class UnlockMasterDatabase : RoomDatabase() {
    abstract fun unlockEventsDao(): UnlockEventsDao
    abstract fun lockEventsDao(): LockEventsDao
    abstract fun screenOnEventsDao(): ScreenOnEventsDao
    abstract fun unlockLimitsDao(): UnlockLimitsDao
    abstract fun screenTimeLimitsDao(): ScreenTimeLimitsDao
    abstract fun counterPausedEventsDao(): CounterPausedEventsDao
    abstract fun counterUnpausedEventsDao(): CounterUnpausedEventsDao

    companion object {
        val MIGRATION_5_6: Migration = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS screen_time_limit (" +
                        "limitApplianceDayTimeInMillis INTEGER PRIMARY KEY NOT NULL, " +
                        "limitAmountMinutes INTEGER NOT NULL)")
            }
        }
    }
}