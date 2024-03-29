package com.sweak.unlockmaster.presentation.introduction.limit_setup.unlock

sealed class UnlockLimitSetupScreenEvent {
    data class PickNewUnlockLimit(val newUnlockLimit: Int) : UnlockLimitSetupScreenEvent()

    data object SubmitSelectedUnlockLimit : UnlockLimitSetupScreenEvent()

    data object ConfirmRemoveUnlockLimitForTomorrow : UnlockLimitSetupScreenEvent()

    data class IsRemoveUnlockLimitForTomorrowDialogVisible(val isVisible: Boolean) :
        UnlockLimitSetupScreenEvent()

    data class IsSettingsNotSavedDialogVisible(val isVisible: Boolean) :
        UnlockLimitSetupScreenEvent()
}
