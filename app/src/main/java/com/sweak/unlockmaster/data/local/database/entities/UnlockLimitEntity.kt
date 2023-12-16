package com.sweak.unlockmaster.data.local.database.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "unlock_limit")
data class UnlockLimitEntity(
    @PrimaryKey val limitApplianceDayTimeInMillis: Long,
    val limitAmount: Int
)
