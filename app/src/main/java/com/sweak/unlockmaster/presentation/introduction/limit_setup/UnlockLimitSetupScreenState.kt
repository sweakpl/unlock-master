package com.sweak.unlockmaster.presentation.introduction.limit_setup

data class UnlockLimitSetupScreenState(
    val pickedUnlockLimit: Int? = null,
    val unlockLimitForTomorrow: Int? = null,
    val isRemoveUnlockLimitForTomorrowDialogVisible: Boolean = false
)