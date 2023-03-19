package com.sweak.unlockmaster.domain.use_case.screen_time

import com.sweak.unlockmaster.domain.model.SessionEvent
import javax.inject.Inject

class GetTodaySessionEventsUseCase @Inject constructor() {

    suspend operator fun invoke(): List<SessionEvent> {
        return listOf(
            SessionEvent.ScreenTime(
                sessionStartTime = 1678345200000,
                sessionEndTime = 1678345260000,
                sessionDuration = 15000
            ),
            SessionEvent.ScreenTime(
                sessionStartTime = 1678350960000,
                sessionEndTime = 1678351260000,
                sessionDuration = 782000
            ),
            SessionEvent.CounterPaused(
                sessionStartTime = 1678351260000,
                sessionEndTime = 1678354320000
            ),
            SessionEvent.ScreenTime(
                sessionStartTime = 1678354320000,
                sessionEndTime = 1678354620000,
                sessionDuration = 300000
            ),
            SessionEvent.ScreenTime(
                sessionStartTime = 1678356720000,
                sessionEndTime = 1678360380000,
                sessionDuration = 3672000
            ),
        )
    }
}