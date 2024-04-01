package com.sweak.unlockmaster.presentation.main.home

import com.github.mikephil.charting.data.BarEntry

data class HomeScreenState(
    val isInitializing: Boolean = true,
    val unlockCount: Int? = null,
    val unlockLimit: Int? = null,
    val isUnlockCounterPaused: Boolean? = null,
    val isUnlockCounterPauseConfirmationDialogVisible: Boolean = false,
    val shouldShowUnlockMasterBlockedWarning: Boolean = false,
    val unlockLimitForTomorrow: Int? = null,
    val todayScreenTimeDurationMillis: Long? = null,
    val isScreenTimeLimitEnabled: Boolean = true,
    val screenTimeLimitMinutes: Int? = null,
    val screenTimeLimitForTomorrowMinutes: Int? = null,
    val lastWeekUnlockEventCounts: List<BarEntry> = emptyList()
)
