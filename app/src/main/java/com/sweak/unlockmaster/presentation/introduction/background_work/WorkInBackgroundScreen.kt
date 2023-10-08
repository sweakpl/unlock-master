package com.sweak.unlockmaster.presentation.introduction.background_work

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.NavigateNext
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.components.Dialog
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.components.OnResume
import com.sweak.unlockmaster.presentation.common.components.ProceedButton
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import com.sweak.unlockmaster.presentation.common.util.navigateThrottled
import com.sweak.unlockmaster.presentation.common.util.popBackStackThrottled

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WorkInBackgroundScreen(
    navController: NavController,
    onWorkInBackgroundAllowed: () -> Unit,
    isLaunchedFromSettings: Boolean
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasUserNavigatedToBackgroundWorkWebsite by remember { mutableStateOf(false) }
    var hasUserFinishedBackgroundWorkInstructions by remember { mutableStateOf(false) }
    var hasUserTriedToGrantNotificationsPermission by remember { mutableStateOf(false) }
    var isNotificationsPermissionDialogVisible by remember { mutableStateOf(false) }

    OnResume {
        if (hasUserNavigatedToBackgroundWorkWebsite) {
            hasUserFinishedBackgroundWorkInstructions = true
        }
    }

    val uriHandler = LocalUriHandler.current
    val backgroundWorkImprovementWebsite = stringResource(R.string.dontkilmyapp_com_full_uri)

    val notificationsPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    } else {
        object : PermissionState {
            override val permission: String get() = "android.permission.POST_NOTIFICATIONS"
            override val status: PermissionStatus = PermissionStatus.Granted
            override fun launchPermissionRequest() { /* no-op */ }
        }
    }

    Column(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
    ) {
        NavigationBar(
            title = stringResource(R.string.work_in_background),
            onBackClick = { navController.popBackStackThrottled(lifecycleOwner) }
        )

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(R.string.allow_work_in_background),
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
                    text = stringResource(R.string.allow_work_in_background_description),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.medium
                        )
                )

                ElevatedCard(
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = MaterialTheme.space.xSmall
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.space.medium)
                        .clickable(
                            enabled =
                            !hasUserFinishedBackgroundWorkInstructions || isLaunchedFromSettings,
                            onClick = {
                                uriHandler.openUri(backgroundWorkImprovementWebsite)
                                hasUserNavigatedToBackgroundWorkWebsite = true
                            }
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(MaterialTheme.space.medium))

                        Icon(
                            imageVector = Icons.Outlined.Build,
                            contentDescription = stringResource(
                                R.string.content_description_wrench_icon
                            ),
                            modifier = Modifier.size(size = MaterialTheme.space.xLarge)
                        )

                        Column(
                            modifier = Modifier
                                .padding(all = MaterialTheme.space.medium)
                                .weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.dontkilmyapp_com),
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.tertiary
                                ),
                                modifier = Modifier.padding(bottom = MaterialTheme.space.xSmall)
                            )

                            Text(
                                text = stringResource(
                                    R.string.select_manufacturer_and_follow_instructions
                                ),
                                style = MaterialTheme.typography.titleSmall
                            )
                        }

                        Icon(
                            imageVector =
                            if (!hasUserFinishedBackgroundWorkInstructions
                                || isLaunchedFromSettings
                            ) {
                                Icons.Outlined.NavigateNext
                            } else Icons.Filled.Done,
                            contentDescription = stringResource(
                                if (!hasUserFinishedBackgroundWorkInstructions
                                    || isLaunchedFromSettings
                                ) {
                                    R.string.content_description_next_icon
                                } else R.string.content_description_done_icon
                            ),
                            modifier = Modifier
                                .size(size = MaterialTheme.space.xLarge)
                                .padding(all = MaterialTheme.space.small)
                        )

                        Spacer(modifier = Modifier.width(MaterialTheme.space.small))
                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Spacer(modifier = Modifier.height(MaterialTheme.space.large))

                    Text(
                        text = stringResource(R.string.allow_notifications),
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
                        text = stringResource(R.string.allow_notifications_description),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(
                                start = MaterialTheme.space.medium,
                                end = MaterialTheme.space.medium,
                                bottom = MaterialTheme.space.medium
                            )
                    )

                    ElevatedCard(
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = MaterialTheme.space.xSmall
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.space.medium)
                            .clickable(
                                enabled =
                                notificationsPermissionState.status !is PermissionStatus.Granted,
                                onClick = {
                                    val permissionStatus = notificationsPermissionState.status

                                    if (permissionStatus is PermissionStatus.Denied) {
                                        if (hasUserTriedToGrantNotificationsPermission &&
                                            !permissionStatus.shouldShowRationale
                                        ) {
                                            isNotificationsPermissionDialogVisible = true
                                        } else {
                                            notificationsPermissionState.launchPermissionRequest()
                                            hasUserTriedToGrantNotificationsPermission = true
                                        }
                                    }
                                }
                            )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.width(MaterialTheme.space.medium))

                            Icon(
                                imageVector =
                                if (notificationsPermissionState.status !is PermissionStatus.Granted)
                                    Icons.Outlined.NotificationsOff
                                else Icons.Outlined.Notifications,
                                contentDescription = stringResource(
                                    R.string.content_description_disabled_notifications_icon
                                ),
                                modifier = Modifier.size(size = MaterialTheme.space.xLarge)
                            )

                            Text(
                                text = stringResource(
                                    if (notificationsPermissionState.status !is PermissionStatus.Granted)
                                        R.string.notifications_disabled_click_to_enable
                                    else R.string.notifications_enabled
                                ),
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier
                                    .padding(all = MaterialTheme.space.medium)
                                    .weight(1f)
                            )

                            Icon(
                                imageVector =
                                if (notificationsPermissionState.status !is PermissionStatus.Granted)
                                    Icons.Outlined.NavigateNext
                                else Icons.Filled.Done,
                                contentDescription = stringResource(
                                    if (notificationsPermissionState.status !is PermissionStatus.Granted)
                                        R.string.content_description_next_icon
                                    else R.string.content_description_done_icon
                                ),
                                modifier = Modifier
                                    .size(size = MaterialTheme.space.xLarge)
                                    .padding(all = MaterialTheme.space.small)
                            )

                            Spacer(modifier = Modifier.width(MaterialTheme.space.small))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.space.run { xLarge + 2 * medium }))
            }

            ProceedButton(
                text = stringResource(R.string._continue),
                onClick = {
                    if (isLaunchedFromSettings) {
                        navController.popBackStackThrottled(lifecycleOwner)
                    } else {
                        onWorkInBackgroundAllowed()
                        navController.navigateThrottled(
                            Screen.SetupCompleteScreen.route,
                            lifecycleOwner
                        )
                    }
                },
                enabled = isLaunchedFromSettings ||
                        (notificationsPermissionState.status is PermissionStatus.Granted &&
                                hasUserFinishedBackgroundWorkInstructions),
                modifier = Modifier.padding(all = MaterialTheme.space.medium)
            )
        }
    }

    val context = LocalContext.current

    if (isNotificationsPermissionDialogVisible) {
        Dialog(
            title = stringResource(R.string.notifications_permission_required),
            message = stringResource(R.string.notifications_permission_required_description),
            onDismissRequest = { isNotificationsPermissionDialogVisible = false },
            onPositiveClick = {
                context.startActivity(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:${context.packageName}")
                    }
                )
                isNotificationsPermissionDialogVisible = false
            },
            positiveButtonText = stringResource(R.string.settings),
            negativeButtonText = stringResource(R.string.later)
        )
    }
}