package com.sweak.unlockmaster.presentation.settings.mobilizing_notifications

data class MobilizingNotificationsScreenState(
    val selectedMobilizingNotificationsFrequencyPercentageIndex: Int? = null,
    val availableMobilizingNotificationsFrequencyPercentages: List<Int>? = null,
    val areOverLimitNotificationsEnabled: Boolean? = null,
    val hasUserChangedAnySettings: Boolean = false,
    val isSettingsNotSavedDialogVisible: Boolean = false
)
