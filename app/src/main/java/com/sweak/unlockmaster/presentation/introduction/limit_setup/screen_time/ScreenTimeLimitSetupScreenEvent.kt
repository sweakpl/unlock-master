package com.sweak.unlockmaster.presentation.introduction.limit_setup.screen_time

sealed class ScreenTimeLimitSetupScreenEvent {
    data class PickNewScreenTimeLimit(
        val newScreenTimeLimitMinutes: Int
    ) : ScreenTimeLimitSetupScreenEvent()

    data class ConfirmSelectedSettings(
        val screenTimeLimitStateChangedCallback: (Boolean) -> Unit
    ) : ScreenTimeLimitSetupScreenEvent()

    data object TryToggleScreenTimeLimitState : ScreenTimeLimitSetupScreenEvent()

    data object DisableScreenTimeLimit : ScreenTimeLimitSetupScreenEvent()

    data class IsScreenTimeLimitDisableConfirmationDialogVisible(val isVisible: Boolean) :
        ScreenTimeLimitSetupScreenEvent()

    data object ConfirmRemoveScreenTimeLimitForTomorrow : ScreenTimeLimitSetupScreenEvent()

    data class IsRemoveScreenTimeLimitForTomorrowDialogVisible(val isVisible: Boolean) :
        ScreenTimeLimitSetupScreenEvent()

    data class IsSettingsNotSavedDialogVisible(val isVisible: Boolean) :
        ScreenTimeLimitSetupScreenEvent()
}
