package com.sweak.unlockmaster.data.local.database.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "screen_on_event")
data class ScreenOnEventEntity(
    @PrimaryKey val timeInMillis: Long
)
