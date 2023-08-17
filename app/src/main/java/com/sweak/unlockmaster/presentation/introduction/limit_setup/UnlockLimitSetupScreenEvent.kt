package com.sweak.unlockmaster.presentation.introduction.limit_setup

sealed class UnlockLimitSetupScreenEvent {
    data class PickNewUnlockLimit(val newUnlockLimit: Int) : UnlockLimitSetupScreenEvent()
    object SubmitSelectedUnlockLimit : UnlockLimitSetupScreenEvent()

    object ConfirmRemoveUnlockLimitForTomorrow : UnlockLimitSetupScreenEvent()

    data class RemoveUnlockLimitForTomorrowDialogVisibilityChanged(val isVisible: Boolean) :
        UnlockLimitSetupScreenEvent()
}
