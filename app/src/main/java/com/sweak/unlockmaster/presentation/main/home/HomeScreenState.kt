package com.sweak.unlockmaster.presentation.main.home

import com.github.mikephil.charting.data.BarEntry

data class HomeScreenState(
    val isInitializing: Boolean = true,
    val unlockCount: Int = -1,
    val unlockLimit: Int = -1,
    val isUnlockCounterPaused: Boolean = false,
    val isUnlockCounterPauseConfirmationDialogVisible: Boolean = false,
    val unlockLimitForTomorrow: Int? = null,
    val todayHoursAndMinutesScreenTimePair: Pair<Int, Int> = Pair(0, 0),
    val lastWeekUnlockEventCounts: List<BarEntry> = emptyList()
)
