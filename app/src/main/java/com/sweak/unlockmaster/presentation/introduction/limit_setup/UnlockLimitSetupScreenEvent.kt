package com.sweak.unlockmaster.presentation.introduction.limit_setup

sealed class UnlockLimitSetupScreenEvent {
    data class NewUnlockLimitPicked(val newUnlockLimit: Int) : UnlockLimitSetupScreenEvent()
    data class SelectedUnlockLimitSubmitted(val isUpdating: Boolean) : UnlockLimitSetupScreenEvent()
}
