package com.sweak.unlockmaster.domain.model

import java.util.*

sealed class SessionEvent(val sessionStartTime: Long, val sessionEndTime: Long) {
    class ScreenTime(
        sessionStartTime: Long,
        sessionEndTime: Long,
        val sessionDuration: Long
    ) : SessionEvent(sessionStartTime, sessionEndTime) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ScreenTime) return false

            return sessionStartTime == other.sessionStartTime &&
                    sessionEndTime == other.sessionEndTime &&
                    sessionDuration == other.sessionDuration
        }

        override fun hashCode(): Int {
            return Objects.hash(sessionStartTime, sessionEndTime, sessionDuration)
        }
    }

    class CounterPaused(
        sessionStartTime: Long,
        sessionEndTime: Long
    ) : SessionEvent(sessionStartTime, sessionEndTime) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is CounterPaused) return false

            return sessionStartTime == other.sessionStartTime &&
                    sessionEndTime == other.sessionEndTime
        }

        override fun hashCode(): Int {
            return Objects.hash(sessionStartTime, sessionEndTime)
        }
    }
}