package com.sweak.unlockmaster.domain.repository

import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.ScreenOnEvent

interface ScreenOnEventsRepository {
    suspend fun addScreenOnEvent(screenOnEvent: ScreenOnEvent)

    suspend fun getLatestScreenOnEvent(): ScreenOnEvent?

    suspend fun getScreenOnEventsSinceTimeAndUntilTime(
        sinceTimeInMillis: Long,
        untilTimeInMillis: Long
    ): List<ScreenOnEvent>
}