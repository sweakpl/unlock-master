package com.sweak.unlockmaster.presentation.settings.data_backup

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.components.Dialog
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.components.ObserveAsEvents
import com.sweak.unlockmaster.presentation.common.theme.space
import com.sweak.unlockmaster.presentation.common.util.popBackStackThrottled

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataBackupScreen(
    navController: NavController,
    dataBackupViewModel: DataBackupViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val backupCreationSuccessfulText = stringResource(R.string.backup_created)
    val dataRestorationSuccessfulText = stringResource(R.string.data_restoration_successful)

    ObserveAsEvents(
        flow = dataBackupViewModel.backupCreationSuccessfulEvents,
        onEvent = {
            Toast.makeText(context, backupCreationSuccessfulText, Toast.LENGTH_LONG).show()
        }
    )

    ObserveAsEvents(
        flow = dataBackupViewModel.dataRestorationSuccessfulEvents,
        onEvent = {
            Toast.makeText(context, dataRestorationSuccessfulText, Toast.LENGTH_LONG).show()
        }
    )

    val createBackupFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
        onResult = { uri ->
            dataBackupViewModel.onEvent(DataBackupScreenEvent.PerformDataBackupCreation(uri))
        }
    )

    val restoreFromBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            dataBackupViewModel.onEvent(DataBackupScreenEvent.PerformDataRestorationFromBackup(uri))
        }
    )

    val dataBackupScreenState = dataBackupViewModel.state
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            NavigationBar(
                title = stringResource(R.string.data_backup),
                onNavigationButtonClick = {
                    if (!dataBackupScreenState.isInTheProcessOfCreatingBackup &&
                        !dataBackupScreenState.isInTheProcessOfRestoringData
                    ) {
                        navController.popBackStackThrottled(lifecycleOwner)
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(paddingValues = paddingValues)) {
                Text(
                    text = stringResource(R.string.data_backup),
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            top = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.small
                        )
                )

                Text(
                    text = stringResource(R.string.data_backup_description),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.mediumLarge
                        )
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.mediumLarge
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = MaterialTheme.space.medium)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = MaterialTheme.space.medium)
                        ) {
                            Text(
                                text = stringResource(R.string.download_backup),
                                style = MaterialTheme.typography.displaySmall
                            )

                            Text(
                                text = stringResource(R.string.download_backup_description),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        AnimatedContent(
                            targetState = dataBackupScreenState.isInTheProcessOfCreatingBackup,
                            label = "backupCreationAnimation",
                            modifier = Modifier.wrapContentWidth()
                        ) {isInTheProcessOfCreatingBackup ->
                            if (!isInTheProcessOfCreatingBackup) {
                                Button(
                                    onClick = {
                                        dataBackupViewModel.onEvent(
                                            DataBackupScreenEvent.CreateBackupClicked(
                                                createBackupFileLauncher = createBackupFileLauncher
                                            )
                                        )
                                    },
                                    enabled = !dataBackupScreenState.isInTheProcessOfRestoringData
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = stringResource(R.string.save),
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier
                                                .padding(end = MaterialTheme.space.small)
                                        )

                                        Icon(
                                            imageVector = Icons.Outlined.FileDownload,
                                            contentDescription = stringResource(
                                                R.string.content_description_download_icon
                                            )
                                        )
                                    }
                                }
                            } else {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(MaterialTheme.space.large)
                                )
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.mediumLarge,
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = MaterialTheme.space.medium)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = MaterialTheme.space.medium)
                        ) {
                            Text(
                                text = stringResource(R.string.restore_data),
                                style = MaterialTheme.typography.displaySmall
                            )

                            Text(
                                text = stringResource(R.string.restore_data_description),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        AnimatedContent(
                            targetState = dataBackupScreenState.isInTheProcessOfRestoringData,
                            label = "backupCreationAnimation",
                            modifier = Modifier.wrapContentWidth()
                        ) { isInTheProcessOfRestoringData ->
                            if (!isInTheProcessOfRestoringData) {
                                Button(
                                    onClick = {
                                        dataBackupViewModel.onEvent(
                                            DataBackupScreenEvent.RestoreDataClicked(
                                                restoreFromBackupLauncher = restoreFromBackupLauncher
                                            )
                                        )
                                    },
                                    enabled = !dataBackupScreenState.isInTheProcessOfCreatingBackup
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = stringResource(R.string.recover),
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier
                                                .padding(end = MaterialTheme.space.small)
                                        )

                                        Icon(
                                            imageVector = Icons.Outlined.FileUpload,
                                            contentDescription = stringResource(
                                                R.string.content_description_upload_icon
                                            )
                                        )
                                    }
                                }
                            } else {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(MaterialTheme.space.large)
                                )
                            }
                        }
                    }
                }

                Text(
                    text = stringResource(R.string.how_does_it_work),
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.small
                        )
                )

                Column(
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.medium
                        )
                ) {
                    Row(modifier = Modifier.padding(bottom = MaterialTheme.space.xSmall)) {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = MaterialTheme.space.xSmall)
                        )

                        Text(
                            text = stringResource(R.string.backup_instructions_1),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Row(modifier = Modifier.padding(bottom = MaterialTheme.space.xSmall)) {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = MaterialTheme.space.xSmall)
                        )

                        Text(
                            text = stringResource(R.string.backup_instructions_2),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Row(modifier = Modifier.padding(bottom = MaterialTheme.space.xSmall)) {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = MaterialTheme.space.xSmall)
                        )

                        Text(
                            text = stringResource(R.string.backup_instructions_3),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Row(modifier = Modifier.padding(bottom = MaterialTheme.space.xSmall)) {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = MaterialTheme.space.xSmall)
                        )

                        Text(
                            text = stringResource(R.string.backup_instructions_4),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Row {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = MaterialTheme.space.xSmall)
                        )

                        Text(
                            text = stringResource(R.string.backup_instructions_5),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.what_to_consider),
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.small
                        )
                )

                Column(
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.medium
                        )
                ) {
                    Row(modifier = Modifier.padding(bottom = MaterialTheme.space.xSmall)) {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = MaterialTheme.space.xSmall)
                        )

                        Text(
                            text = stringResource(R.string.backup_considerations_1),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Row {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = MaterialTheme.space.xSmall)
                        )

                        Text(
                            text = stringResource(R.string.backup_considerations_2),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }

    if (dataBackupScreenState.isCounterPausedErrorDialogVisible) {
        Dialog(
            title = stringResource(R.string.backup_unavailable),
            message = stringResource(R.string.backup_unavailable_description),
            onDismissRequest = {
                dataBackupViewModel.onEvent(
                    DataBackupScreenEvent.IsCounterPausedErrorDialogVisible(isVisible = false)
                )
            },
            onPositiveClick = {
                dataBackupViewModel.onEvent(
                    DataBackupScreenEvent.IsCounterPausedErrorDialogVisible(isVisible = false)
                )
            },
            positiveButtonText = stringResource(R.string.ok)
        )
    }

    if (dataBackupScreenState.isBackupCreationErrorDialogVisible) {
        Dialog(
            title = stringResource(R.string.backup_not_created),
            message = stringResource(R.string.backup_not_created_description),
            onDismissRequest = {
                dataBackupViewModel.onEvent(
                    DataBackupScreenEvent.IsBackupCreationErrorDialogVisible(isVisible = false)
                )
            },
            onPositiveClick = {
                dataBackupViewModel.onEvent(
                    DataBackupScreenEvent.IsBackupCreationErrorDialogVisible(isVisible = false)
                )
            },
            positiveButtonText = stringResource(R.string.ok)
        )
    }

    if (dataBackupScreenState.isDataRestorationErrorDialogVisible) {
        Dialog(
            title = stringResource(R.string.data_restoration_unsuccessful),
            message = stringResource(R.string.data_restoration_unsuccessful_description),
            onDismissRequest = {
                dataBackupViewModel.onEvent(
                    DataBackupScreenEvent.IsDataRestorationErrorDialogVisible(isVisible = false)
                )
            },
            onPositiveClick = {
                dataBackupViewModel.onEvent(
                    DataBackupScreenEvent.IsDataRestorationErrorDialogVisible(isVisible = false)
                )
            },
            positiveButtonText = stringResource(R.string.ok)
        )
    }
}