package com.sweak.unlockmaster.data.local.database.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "counter_unpaused_event")
data class CounterUnpausedEventEntity(
    @PrimaryKey val timeInMillis: Long
)
