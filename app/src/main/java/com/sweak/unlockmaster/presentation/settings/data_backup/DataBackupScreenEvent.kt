package com.sweak.unlockmaster.presentation.settings.data_backup

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher

sealed class DataBackupScreenEvent {
    data class CreateBackupClicked(
        val createBackupFileLauncher: ManagedActivityResultLauncher<String, Uri?>
    ) : DataBackupScreenEvent()

    data class PerformDataBackupCreation(val dataBackupFileUri: Uri?) : DataBackupScreenEvent()

    data class RestoreDataClicked(
        val restoreFromBackupLauncher: ManagedActivityResultLauncher<Array<String>, Uri?>
    ) : DataBackupScreenEvent()

    data class PerformDataRestorationFromBackup(val dataBackupFileUri: Uri?) :
        DataBackupScreenEvent()

    data class IsCounterPausedErrorDialogVisible(val isVisible: Boolean) : DataBackupScreenEvent()

    data class IsBackupCreationErrorDialogVisible(val isVisible: Boolean) : DataBackupScreenEvent()

    data class IsDataRestorationErrorDialogVisible(val isVisible: Boolean) : DataBackupScreenEvent()
}