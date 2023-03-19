package com.sweak.unlockmaster.presentation.main.screen_time

import com.github.mikephil.charting.data.Entry

data class ScreenTimeScreenState(
    val isInitializing: Boolean = true,
    val screenTimeMinutesPerHourEntries: List<Entry> = emptyList(),
    val todayHoursAndMinutesScreenTimePair: Pair<Int, Int> = Pair(0, 0),
    val UIReadySessionEvents: List<UIReadySessionEvent> = emptyList()
) {
    sealed class UIReadySessionEvent(val startAndEndTimesInMillis: Pair<Long, Long>) {
        class ScreenTime(
            screenSessionStartAndEndTimesInMillis: Pair<Long, Long>,
            val screenSessionHoursMinutesAndSecondsDurationTriple: Triple<Int, Int, Int>
        ) : UIReadySessionEvent(screenSessionStartAndEndTimesInMillis)

        class CounterPaused(
            counterPauseSessionStartAndEndTimesInMillis: Pair<Long, Long>
        ) : UIReadySessionEvent(counterPauseSessionStartAndEndTimesInMillis)
    }
}
