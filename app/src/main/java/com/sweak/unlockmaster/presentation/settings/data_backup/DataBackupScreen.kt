package com.sweak.unlockmaster.presentation.settings.data_backup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.theme.UnlockMasterTheme
import com.sweak.unlockmaster.presentation.common.theme.space
import com.sweak.unlockmaster.presentation.common.util.popBackStackThrottled

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataBackupScreen(navController: NavController) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            NavigationBar(
                title = stringResource(R.string.data_backup),
                onNavigationButtonClick = { navController.popBackStackThrottled(lifecycleOwner) },
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

                        Button(
                            onClick = { /* TODO */ },
                            modifier = Modifier.wrapContentWidth()
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
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.space.medium)
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

                        Button(
                            onClick = { /* TODO */ },
                            modifier = Modifier.wrapContentWidth()
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
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun DataBackupScreenPreview() {
    UnlockMasterTheme {
        DataBackupScreen(NavController(LocalContext.current))
    }
}