package com.sweak.unlockmaster.domain.repository

import com.sweak.unlockmaster.domain.model.ScreenOnEvent

interface ScreenOnEventsRepository {
    suspend fun addScreenOnEvent(screenOnEventTimeInMillis: Long)

    suspend fun getLatestScreenOnEvent(): ScreenOnEvent?
}