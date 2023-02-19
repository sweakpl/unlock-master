package com.sweak.unlockmaster.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "unlock_limit")
data class UnlockLimitEntity(
    @PrimaryKey val limitApplianceDayTimeInMillis: Long,
    val limitAmount: Int
)
