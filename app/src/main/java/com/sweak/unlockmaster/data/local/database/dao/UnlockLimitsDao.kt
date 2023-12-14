package com.sweak.unlockmaster.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sweak.unlockmaster.data.local.database.entities.UnlockLimitEntity

@Dao
interface UnlockLimitsDao {

    @Insert
    suspend fun insert(unlockLimitEntity: UnlockLimitEntity)

    @Insert
    suspend fun insertAll(unlockLimitsEntities: List<UnlockLimitEntity>)

    @Update
    suspend fun update(unlockLimitEntity: UnlockLimitEntity)

    @Delete
    suspend fun delete(unlockLimitEntity: UnlockLimitEntity)

    @Query("SELECT * FROM unlock_limit")
    suspend fun getAllUnlockLimits(): List<UnlockLimitEntity>

    @Delete
    suspend fun deleteAll(unlockLimitsEntities: List<UnlockLimitEntity>)
}