package com.sweak.unlockmaster.presentation.settings.daily_wrap_up_settings

data class DailyWrapUpSettingsScreenState(
    val notificationHourOfDay: Int? = null,
    val notificationMinute: Int? = null,
    val isInvalidTimeSelectedDialogVisible: Boolean = false,
    val hasInitialTimeBeenSet: Boolean = false,
    val hasUserChangedAnySettings: Boolean = false,
    val isSettingsNotSavedDialogVisible: Boolean = false
)
