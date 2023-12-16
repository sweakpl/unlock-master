package com.sweak.unlockmaster.presentation.settings.data_backup

data class DataBackupScreenState(
    val isInTheProcessOfCreatingBackup: Boolean = false,
    val isInTheProcessOfRestoringData: Boolean = false,
    val isCounterPausedErrorDialogVisible: Boolean = false,
    val isBackupCreationErrorDialogVisible: Boolean = false,
    val isDataRestorationErrorDialogVisible: Boolean = false
)
