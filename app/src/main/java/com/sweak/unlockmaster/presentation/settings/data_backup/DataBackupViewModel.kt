package com.sweak.unlockmaster.presentation.settings.data_backup

import android.content.ContentResolver
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import com.sweak.unlockmaster.domain.management.UnlockMasterBackupManager
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DataBackupViewModel @Inject constructor(
    private val unlockMasterBackupManager: UnlockMasterBackupManager,
    private val contentResolver: ContentResolver,
    private val userSessionRepository: UserSessionRepository
) : ViewModel() {

    var state by mutableStateOf(DataBackupScreenState())

    private val backupCreationSuccessfulEventsChannel =
        Channel<BackupCreationSuccessfulEvent>()
    val backupCreationSuccessfulEvents =
        backupCreationSuccessfulEventsChannel.receiveAsFlow()

    private val dataRestorationSuccessfulEventsChannel =
        Channel<DataRestorationSuccessfulEvent>()
    val dataRestorationSuccessfulEvents =
        dataRestorationSuccessfulEventsChannel.receiveAsFlow()

    fun onEvent(event: DataBackupScreenEvent) {
        when (event) {
            is DataBackupScreenEvent.CreateBackupClicked -> {
                state = state.copy(isInTheProcessOfCreatingBackup = true)
                event.createBackupFileLauncher.launch("UnlockMasterBackup.json")
            }
            is DataBackupScreenEvent.RestoreDataClicked -> {
                if (runBlocking { userSessionRepository.isUnlockCounterPaused() }) {
                    state = state.copy(isCounterPausedErrorDialogVisible = true)
                    return
                }

                state = state.copy(isInTheProcessOfRestoringData = true)
                event.restoreFromBackupLauncher.launch(arrayOf("application/json"))
            }
            is DataBackupScreenEvent.PerformDataBackupCreation -> {
                val uri = event.dataBackupFileUri ?: run {
                    state = state.copy(
                        isInTheProcessOfCreatingBackup = false,
                        isBackupCreationErrorDialogVisible = true
                    )
                    return
                }

                viewModelScope.launch {
                    try {
                        contentResolver.openFileDescriptor(uri, "w")?.use { fileDescriptor ->
                            FileOutputStream(fileDescriptor.fileDescriptor).use { outputStream ->
                                withContext(Dispatchers.IO) {
                                    outputStream.write(
                                        unlockMasterBackupManager.createDataBackupFile()
                                    )
                                }
                            }
                        }
                    } catch (fileNotFoundException: FileNotFoundException) {
                        state = state.copy(isBackupCreationErrorDialogVisible = true)
                    } catch (ioException: IOException) {
                        state = state.copy(isBackupCreationErrorDialogVisible = true)
                    } catch (jsonIoException: JsonIOException) {
                        state = state.copy(isBackupCreationErrorDialogVisible = true)
                    }

                    state = state.copy(isInTheProcessOfCreatingBackup = false)

                    if (!state.isBackupCreationErrorDialogVisible) {
                        backupCreationSuccessfulEventsChannel.send(BackupCreationSuccessfulEvent)
                    }
                }
            }
            is DataBackupScreenEvent.PerformDataRestorationFromBackup -> {
                val uri = event.dataBackupFileUri ?: run {
                    state = state.copy(
                        isInTheProcessOfRestoringData = false,
                        isDataRestorationErrorDialogVisible = true
                    )
                    return
                }

                viewModelScope.launch {
                    try {
                        contentResolver.openInputStream(uri)?.use { inputStream ->
                            val byteArrayOutputStream = ByteArrayOutputStream()
                            val buffer = ByteArray(4096)
                            var bytesRead: Int

                            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                                byteArrayOutputStream.write(buffer, 0, bytesRead)
                            }

                            withContext(Dispatchers.IO) {
                                unlockMasterBackupManager.restoreDataFromBackupFile(
                                    byteArrayOutputStream.toByteArray()
                                )
                            }
                        }
                    } catch (fileNotFoundException: FileNotFoundException) {
                        state = state.copy(isDataRestorationErrorDialogVisible = true)
                    } catch (ioException: IOException) {
                        state = state.copy(isDataRestorationErrorDialogVisible = true)
                    } catch (jsonIoException: JsonIOException) {
                        state = state.copy(isDataRestorationErrorDialogVisible = true)
                    } catch (jsonIoException: JsonSyntaxException) {
                        state = state.copy(isDataRestorationErrorDialogVisible = true)
                    }

                    state = state.copy(isInTheProcessOfRestoringData = false)

                    if (!state.isDataRestorationErrorDialogVisible) {
                        dataRestorationSuccessfulEventsChannel.send(DataRestorationSuccessfulEvent)
                    }
                }
            }
            is DataBackupScreenEvent.IsCounterPausedErrorDialogVisible ->
                state = state.copy(isCounterPausedErrorDialogVisible = event.isVisible)
            is DataBackupScreenEvent.IsBackupCreationErrorDialogVisible ->
                state = state.copy(isBackupCreationErrorDialogVisible = event.isVisible)
            is DataBackupScreenEvent.IsDataRestorationErrorDialogVisible ->
                state = state.copy(isDataRestorationErrorDialogVisible = event.isVisible)
        }
    }

    object BackupCreationSuccessfulEvent
    object DataRestorationSuccessfulEvent
}