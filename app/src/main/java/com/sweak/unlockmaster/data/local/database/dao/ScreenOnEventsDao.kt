package com.sweak.unlockmaster.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.sweak.unlockmaster.data.local.database.entities.ScreenOnEventEntity

@Dao
interface ScreenOnEventsDao {

    @Insert
    suspend fun insert(screenOnEventEntity: ScreenOnEventEntity)

    @Insert
    suspend fun insertAll(screenOnEventsEntities: List<ScreenOnEventEntity>)

    @Query("SELECT * FROM screen_on_event")
    suspend fun getAllScreenOnEvents(): List<ScreenOnEventEntity>

    @Delete
    suspend fun deleteAll(screenOnEventsEntities: List<ScreenOnEventEntity>)
}