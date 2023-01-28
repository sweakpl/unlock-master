package com.sweak.unlockmaster.presentation.main.home

data class HomeScreenState(
    val isInitializing: Boolean = true,
    val unlockCount: Int = -1,
    val unlockLimit: Int = -1,
    val isUnlockCounterPaused: Boolean = false,
    val unlockLimitForTomorrow: Int? = null
)
