package com.sweak.unlockmaster.data.local.database.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "unlock_event")
data class UnlockEventEntity(
    @PrimaryKey val timeInMillis: Long
)
