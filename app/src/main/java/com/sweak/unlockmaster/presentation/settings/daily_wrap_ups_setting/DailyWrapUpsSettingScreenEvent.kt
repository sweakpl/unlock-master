package com.sweak.unlockmaster.presentation.settings.daily_wrap_ups_setting

sealed class DailyWrapUpsSettingScreenEvent {
    data class SelectNewDailyWrapUpsSettingNotificationsTime(
        val newNotificationHourOfDay: Int,
        val newNotificationMinute: Int,
    ) : DailyWrapUpsSettingScreenEvent()

    object ConfirmNewSelectedDailyWrapUpsNotificationsTimeSetting : DailyWrapUpsSettingScreenEvent()

    data class IsInvalidTimeSelectedDialogVisible(val isVisible: Boolean) :
        DailyWrapUpsSettingScreenEvent()
}