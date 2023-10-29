package com.sweak.unlockmaster.presentation.introduction.background_work

sealed class WorkInBackgroundScreenEvent {
    data object CheckIfIgnoringBatteryOptimizations : WorkInBackgroundScreenEvent()

    data class IsIgnoreBatteryOptimizationsRequestUnavailableDialogVisible(
        val isVisible: Boolean
    ) : WorkInBackgroundScreenEvent()

    data object UserTriedToGrantNotificationsPermission : WorkInBackgroundScreenEvent()

    data class IsNotificationsPermissionDialogVisible(
        val isVisible: Boolean
    ) : WorkInBackgroundScreenEvent()

    data class IsWebBrowserNotFoundDialogVisible(
        val isVisible: Boolean
    ) : WorkInBackgroundScreenEvent()
}
