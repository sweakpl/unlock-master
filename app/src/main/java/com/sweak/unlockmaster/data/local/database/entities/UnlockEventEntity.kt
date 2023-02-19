package com.sweak.unlockmaster.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "unlock_event")
data class UnlockEventEntity(
    @PrimaryKey val timeInMillis: Long
)
