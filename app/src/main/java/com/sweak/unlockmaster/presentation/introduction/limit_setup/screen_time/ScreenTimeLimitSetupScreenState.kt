package com.sweak.unlockmaster.presentation.introduction.limit_setup.screen_time

data class ScreenTimeLimitSetupScreenState(
    val pickedScreenTimeLimitMinutes: Int? = null,
    val availableScreenTimeLimitRange: IntRange? = null,
    val screenTimeLimitIntervalMinutes: Int? = null,
    val isScreenTimeLimitEnabled: Boolean = true,
    val screenTimeLimitMinutesForTomorrow: Int? = null,
    val isRemoveScreenTimeLimitForTomorrowDialogVisible: Boolean = false,
    val hasUserChangedAnySettings: Boolean = false,
    val isSettingsNotSavedDialogVisible: Boolean = false
)