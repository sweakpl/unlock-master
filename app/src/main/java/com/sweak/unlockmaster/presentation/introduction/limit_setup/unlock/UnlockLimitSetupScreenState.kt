package com.sweak.unlockmaster.presentation.introduction.limit_setup.unlock

data class UnlockLimitSetupScreenState(
    val pickedUnlockLimit: Int? = null,
    val availableUnlockLimitRange: IntRange? = null,
    val unlockLimitForTomorrow: Int? = null,
    val isRemoveUnlockLimitForTomorrowDialogVisible: Boolean = false,
    val hasUserChangedAnySettings: Boolean = false,
    val isSettingsNotSavedDialogVisible: Boolean = false
)