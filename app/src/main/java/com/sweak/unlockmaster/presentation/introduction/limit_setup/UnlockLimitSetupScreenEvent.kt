package com.sweak.unlockmaster.presentation.introduction.limit_setup

sealed class UnlockLimitSetupScreenEvent {
    data class PickNewUnlockLimit(val newUnlockLimit: Int) : UnlockLimitSetupScreenEvent()
    data object SubmitSelectedUnlockLimit : UnlockLimitSetupScreenEvent()

    data object ConfirmRemoveUnlockLimitForTomorrow : UnlockLimitSetupScreenEvent()

    data class RemoveUnlockLimitForTomorrowDialogVisibilityChanged(val isVisible: Boolean) :
        UnlockLimitSetupScreenEvent()
}
