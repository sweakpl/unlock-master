package com.sweak.unlockmaster.data.local.database.dao

import androidx.room.*
import com.sweak.unlockmaster.data.local.database.entities.UnlockLimitEntity

@Dao
interface UnlockLimitsDao {

    @Insert
    suspend fun insert(unlockLimitEntity: UnlockLimitEntity)

    @Update
    suspend fun update(unlockLimitEntity: UnlockLimitEntity)

    @Delete
    suspend fun delete(unlockLimitEntity: UnlockLimitEntity)

    @Query("SELECT * FROM unlock_limit")
    suspend fun getAllUnlockLimits(): List<UnlockLimitEntity>
}