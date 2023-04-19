package com.sweak.unlockmaster.presentation.main.statistics

import com.github.mikephil.charting.data.BarEntry

data class StatisticsScreenState(
    val allTimeUnlockEventCounts: List<BarEntry> = emptyList(),
    val currentlyHighlightedDayTimeInMillis: Long = System.currentTimeMillis(),
    val unlockEventsCount: Int = 0,
    val unlockLimitAmount: Int = 0,
    val screenOnEventsCount: Int = 0,
    val hoursAndMinutesScreenTimePair: Pair<Int, Int> = Pair(0, 0),
    val isScreenOnEventsInformationDialogVisible: Boolean = false
)
