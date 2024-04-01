package com.sweak.unlockmaster.presentation.main.screen_time

import com.github.mikephil.charting.data.Entry

data class ScreenTimeScreenState(
    val isInitializing: Boolean = true,
    val screenTimeMinutesPerHourEntries: List<Entry> = emptyList(),
    val todayScreenTimeDurationMillis: Long? = null,
    val uiReadySessionEvents: List<UIReadySessionEvent> = emptyList()
) {
    sealed class UIReadySessionEvent(val startAndEndTimesInMillis: Pair<Long, Long>) {
        class ScreenTime(
            screenSessionStartAndEndTimesInMillis: Pair<Long, Long>,
            val screenSessionDurationMillis: Long
        ) : UIReadySessionEvent(screenSessionStartAndEndTimesInMillis)

        class CounterPaused(
            counterPauseSessionStartAndEndTimesInMillis: Pair<Long, Long>
        ) : UIReadySessionEvent(counterPauseSessionStartAndEndTimesInMillis)
    }
}
