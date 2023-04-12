package com.sweak.unlockmaster.presentation.main.statistics

import com.github.mikephil.charting.data.BarEntry

data class StatisticsScreenState(
    val allTimeUnlockEventCounts: List<BarEntry> = emptyList(),
    val currentlyHighlightedDayTimeInMillis: Long = System.currentTimeMillis(),
    val isScreenOnEventsInformationDialogVisible: Boolean = false
)
