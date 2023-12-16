package com.sweak.unlockmaster.data.local.database.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "counter_paused_event")
data class CounterPausedEventEntity(
    @PrimaryKey val timeInMillis: Long
)
