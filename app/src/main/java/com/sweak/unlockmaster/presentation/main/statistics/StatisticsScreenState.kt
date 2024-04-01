package com.sweak.unlockmaster.presentation.main.statistics

import com.github.mikephil.charting.data.BarEntry

data class StatisticsScreenState(
    val isInitializing: Boolean = true,
    val allTimeUnlockEventCounts: List<BarEntry> = emptyList(),
    val currentlyHighlightedDayTimeInMillis: Long = System.currentTimeMillis(),
    val unlockEventsCount: Int = 0,
    val unlockLimitAmount: Int = 0,
    val screenOnEventsCount: Int = 0,
    val screenTimeDurationMillis: Long? = null,
    val screenTimeLimitDurationMillis: Long? = null,
    val isScreenOnEventsInformationDialogVisible: Boolean = false
)
