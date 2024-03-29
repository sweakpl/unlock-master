package com.sweak.unlockmaster.data.local.database.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "screen_time_limit")
data class ScreenTimeLimitEntity(
    @PrimaryKey val limitApplianceDayTimeInMillis: Long,
    val limitAmountMinutes: Int
)
