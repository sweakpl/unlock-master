package com.sweak.unlockmaster.presentation.settings.daily_wrap_ups

sealed class DailyWrapUpsScreenEvent {
    data class SelectNewDailyWrapUpsNotificationsTime(
        val newNotificationHourOfDay: Int,
        val newNotificationMinute: Int,
    ) : DailyWrapUpsScreenEvent()

    object ConfirmNewSelectedDailyWrapUpsNotificationsTime : DailyWrapUpsScreenEvent()
}