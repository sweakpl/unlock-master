package com.sweak.unlockmaster.presentation.settings.application_blocked

sealed class ApplicationBlockedScreenEvent {
    data object CheckIfIgnoringBatteryOptimizations : ApplicationBlockedScreenEvent()

    data class IsIgnoreBatteryOptimizationsRequestUnavailableDialogVisible(
        val isVisible: Boolean
    ) : ApplicationBlockedScreenEvent()

    data class IsWebBrowserNotFoundDialogVisible(
        val isVisible: Boolean
    ) : ApplicationBlockedScreenEvent()
}
