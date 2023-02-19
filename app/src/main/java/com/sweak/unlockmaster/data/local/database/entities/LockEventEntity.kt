package com.sweak.unlockmaster.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lock_event")
data class LockEventEntity(
    @PrimaryKey val timeInMillis: Long
)
