package com.sweak.unlockmaster.presentation.settings.daily_wrap_ups_setting

data class DailyWrapUpsSettingScreenState(
    val notificationHourOfDay: Int? = null,
    val notificationMinute: Int? = null,
    val isInvalidTimeSelectedDialogVisible: Boolean = false
)
