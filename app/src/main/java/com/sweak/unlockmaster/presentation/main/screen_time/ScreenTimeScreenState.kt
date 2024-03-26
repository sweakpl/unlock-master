package com.sweak.unlockmaster.presentation.main.screen_time

import com.github.mikephil.charting.data.Entry
import com.sweak.unlockmaster.presentation.common.util.Duration

data class ScreenTimeScreenState(
    val isInitializing: Boolean = true,
    val screenTimeMinutesPerHourEntries: List<Entry> = emptyList(),
    val todayScreenTimeDuration: Duration? = null,
    val uiReadySessionEvents: List<UIReadySessionEvent> = emptyList()
) {
    sealed class UIReadySessionEvent(val startAndEndTimesInMillis: Pair<Long, Long>) {
        class ScreenTime(
            screenSessionStartAndEndTimesInMillis: Pair<Long, Long>,
            val screenSessionDuration: Duration
        ) : UIReadySessionEvent(screenSessionStartAndEndTimesInMillis)

        class CounterPaused(
            counterPauseSessionStartAndEndTimesInMillis: Pair<Long, Long>
        ) : UIReadySessionEvent(counterPauseSessionStartAndEndTimesInMillis)
    }
}
