package com.sweak.unlockmaster.presentation.introduction.background_work

data class WorkInBackgroundScreenState(
    val isIgnoringBatteryOptimizations: Boolean = false,
    val isIgnoreBatteryOptimizationsRequestUnavailable: Boolean = false,
    val isIgnoreBatteryOptimizationsRequestUnavailableDialogVisible: Boolean = false,
    val hasUserTriedToGrantNotificationsPermission: Boolean = false,
    val isNotificationsPermissionDialogVisible: Boolean = false
)
