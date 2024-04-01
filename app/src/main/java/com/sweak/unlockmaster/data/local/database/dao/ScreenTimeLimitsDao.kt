package com.sweak.unlockmaster.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sweak.unlockmaster.data.local.database.entities.ScreenTimeLimitEntity

@Dao
interface ScreenTimeLimitsDao {

    @Insert
    suspend fun insert(screenTimeLimitEntity: ScreenTimeLimitEntity)

    @Insert
    suspend fun insertAll(screenTimeLimitsEntities: List<ScreenTimeLimitEntity>)

    @Update
    suspend fun update(screenTimeLimitEntity: ScreenTimeLimitEntity)

    @Delete
    suspend fun delete(screenTimeLimitEntity: ScreenTimeLimitEntity)

    @Query("SELECT * FROM screen_time_limit")
    suspend fun getAllScreenTimeLimits(): List<ScreenTimeLimitEntity>

    @Delete
    suspend fun deleteAll(screenTimeLimitsEntities: List<ScreenTimeLimitEntity>)
}