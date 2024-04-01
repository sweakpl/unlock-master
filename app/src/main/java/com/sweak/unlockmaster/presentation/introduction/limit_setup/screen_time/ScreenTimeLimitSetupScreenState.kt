package com.sweak.unlockmaster.presentation.introduction.limit_setup.screen_time

data class ScreenTimeLimitSetupScreenState(
    val isScreenTimeLimitEnabled: Boolean? = null,
    val pickedScreenTimeLimitMinutes: Int? = null,
    val availableScreenTimeLimitRange: IntRange? = null,
    val screenTimeLimitIntervalMinutes: Int? = null,
    val screenTimeLimitMinutesForTomorrow: Int? = null,
    val isRemoveScreenTimeLimitForTomorrowDialogVisible: Boolean = false,
    val isScreenTimeLimitDisableConfirmationDialogVisible: Boolean = false,
    val hasUserChangedAnySettings: Boolean = false,
    val isSettingsNotSavedDialogVisible: Boolean = false
)