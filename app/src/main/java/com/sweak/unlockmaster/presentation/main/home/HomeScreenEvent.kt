package com.sweak.unlockmaster.presentation.main.home

sealed class HomeScreenEvent {
    data class TryPauseOrUnpauseUnlockCounter(
        val pauseChangedCallback: (Boolean) -> Unit
    ) : HomeScreenEvent()
    data class PauseUnlockCounter(
        val pauseChangedCallback: (Boolean) -> Unit
    ) : HomeScreenEvent()
    data class IsUnlockCounterPauseConfirmationDialogVisible(
        val isVisible: Boolean
    ) : HomeScreenEvent()
    data object DismissUnlockMasterBlockedWarning : HomeScreenEvent()
}
