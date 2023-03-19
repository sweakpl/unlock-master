package com.sweak.unlockmaster.domain.model

sealed class SessionEvent(val sessionStartTime: Long, val sessionEndTime: Long) {
    class ScreenTime(
        sessionStartTime: Long,
        sessionEndTime: Long,
        val sessionDuration: Long
    ) : SessionEvent(sessionStartTime, sessionEndTime)

    class CounterPaused(
        sessionStartTime: Long,
        sessionEndTime: Long
    ) : SessionEvent(sessionStartTime, sessionEndTime)
}