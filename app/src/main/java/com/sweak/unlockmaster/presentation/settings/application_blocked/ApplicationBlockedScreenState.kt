package com.sweak.unlockmaster.presentation.settings.application_blocked

data class ApplicationBlockedScreenState(
    val isIgnoringBatteryOptimizations: Boolean = false,
    val isIgnoreBatteryOptimizationsRequestUnavailable: Boolean = false,
    val isIgnoreBatteryOptimizationsRequestUnavailableDialogVisible: Boolean = false,
    val isWebBrowserNotFoundDialogVisible: Boolean = false
)
