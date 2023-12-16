package com.sweak.unlockmaster.data.local.database.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "lock_event")
data class LockEventEntity(
    @PrimaryKey val timeInMillis: Long
)
