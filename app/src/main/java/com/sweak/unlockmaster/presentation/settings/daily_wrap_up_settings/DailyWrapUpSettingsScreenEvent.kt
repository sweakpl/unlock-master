package com.sweak.unlockmaster.presentation.settings.daily_wrap_up_settings

sealed class DailyWrapUpSettingsScreenEvent {
    data class SelectNewDailyWrapUpSettingsNotificationsTime(
        val newNotificationHourOfDay: Int,
        val newNotificationMinute: Int,
    ) : DailyWrapUpSettingsScreenEvent()

    data object ConfirmNewSelectedDailyWrapUpSettings : DailyWrapUpSettingsScreenEvent()

    data class IsInvalidTimeSelectedDialogVisible(val isVisible: Boolean) :
        DailyWrapUpSettingsScreenEvent()
}