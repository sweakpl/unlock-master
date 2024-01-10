package com.sweak.unlockmaster.presentation.settings.mobilizing_notifications

sealed class MobilizingNotificationsScreenEvent {
    data class SelectNewFrequencyPercentageIndex(
        val newPercentageIndex: Int
    ) : MobilizingNotificationsScreenEvent()

    data class ToggleOverLimitNotifications(
        val areOverLimitNotificationsEnabled: Boolean
    ) : MobilizingNotificationsScreenEvent()
    data object ConfirmSelectedSettings : MobilizingNotificationsScreenEvent()
}
