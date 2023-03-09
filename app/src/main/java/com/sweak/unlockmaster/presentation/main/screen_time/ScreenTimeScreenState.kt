package com.sweak.unlockmaster.presentation.main.screen_time

import com.github.mikephil.charting.data.Entry

data class ScreenTimeScreenState(
    val isInitializing: Boolean = true,
    val screenTimeMinutesPerHourEntries: List<Entry> = emptyList(),
    val todayHoursAndMinutesScreenTimePair: Pair<Int, Int> = Pair(0, 0),
    val sessionEvents: List<SessionEvent> = emptyList()
) {
    sealed class SessionEvent {
        data class ScreenTimeSessionEvent(
            val screenSessionStartAndEndTimesInMillis: Pair<Long, Long>,
            val screenSessionHoursMinutesAndSecondsDurationTriple: Triple<Int, Int, Int>
        ) : SessionEvent()
        data class CounterPausedSessionEvent(
            val counterPauseSessionStartAndEndTimesInMillis: Pair<Long, Long>
        ) : SessionEvent()
    }
}
