package com.sweak.unlockmaster.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sweak.unlockmaster.data.local.database.entities.ScreenOnEventEntity

@Dao
interface ScreenOnEventsDao {

    @Insert
    suspend fun insert(screenOnEventEntity: ScreenOnEventEntity)

    @Query("SELECT * FROM screen_on_event ORDER BY timeInMillis DESC LIMIT 1")
    suspend fun getLatestScreenOnEvent(): ScreenOnEventEntity?

    @Query(
        "SELECT * FROM screen_on_event " +
                "WHERE timeInMillis >= :sinceTimeInMillis AND timeInMillis < :untilTimeInMillis"
    )
    suspend fun getScreenOnEventsSinceTimeUntilTime(
        sinceTimeInMillis: Long,
        untilTimeInMillis: Long
    ): List<ScreenOnEventEntity>
}