package com.sweak.unlockmaster.presentation.main.home

import com.github.mikephil.charting.data.BarEntry
import com.sweak.unlockmaster.presentation.common.util.Duration

data class HomeScreenState(
    val isInitializing: Boolean = true,
    val unlockCount: Int? = null,
    val unlockLimit: Int? = null,
    val isUnlockCounterPaused: Boolean? = null,
    val isUnlockCounterPauseConfirmationDialogVisible: Boolean = false,
    val shouldShowUnlockMasterBlockedWarning: Boolean = false,
    val unlockLimitForTomorrow: Int? = null,
    val todayScreenTimeDuration: Duration? = null,
    val lastWeekUnlockEventCounts: List<BarEntry> = emptyList()
)
