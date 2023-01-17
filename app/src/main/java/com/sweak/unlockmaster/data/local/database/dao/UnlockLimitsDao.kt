package com.sweak.unlockmaster.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sweak.unlockmaster.data.local.database.entities.UnlockLimit

@Dao
interface UnlockLimitsDao {

    @Insert
    suspend fun insert(unlockLimit: UnlockLimit)

    @Update
    suspend fun update(unlockLimit: UnlockLimit)

    @Query(
        "SELECT limitAmount FROM unlock_limit WHERE limitApplianceDayTimeInMillis = " +
                "(SELECT MAX(limitApplianceDayTimeInMillis) FROM unlock_limit " +
                "WHERE limitApplianceDayTimeInMillis < :currentTimeInMillis)"
    )
    suspend fun getCurrentUnlockLimit(currentTimeInMillis: Long): Int?
}