package com.sweak.unlockmaster.presentation.introduction.background_work

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.NavigateNext
import androidx.compose.material.icons.outlined.AppSettingsAlt
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.SettingsSuggest
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.sweak.unlockmaster.presentation.common.theme.space
import com.sweak.unlockmaster.presentation.common.util.navigateThrottled
import com.sweak.unlockmaster.presentation.common.util.popBackStackThrottled

@SuppressLint("BatteryLife")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WorkInBackgroundScreen(
    navController: NavController,
    onWorkInBackgroundAllowed: () -> Unit,
    isLaunchedFromSettings: Boolean,
    workInBackgroundViewModel: WorkInBackgroundViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val workInBackgroundScreenState = workInBackgroundViewModel.state

    var hasUserNavigatedToBackgroundWorkWebsite by remember { mutableStateOf(false) }
    var hasUserFinishedBackgroundWorkInstructions by remember { mutableStateOf(false) }

    OnResume {
        if (hasUserNavigatedToBackgroundWorkWebsite) {
            hasUserFinishedBackgroundWorkInstructions = true
        }

        workInBackgroundViewModel.onEvent(
            WorkInBackgroundScreenEvent.CheckIfIgnoringBatteryOptimizations
        )
    }

    val context = LocalContext.current
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

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(
            connection = scrollBehavior.nestedScrollConnection
        ),
        topBar = {
            NavigationBar(
                title = stringResource(R.string.work_in_background),
                onNavigationButtonClick = { navController.popBackStackThrottled(lifecycleOwner) },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
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
                                hasUserFinishedBackgroundWorkInstructions &&
                                (workInBackgroundScreenState.isIgnoringBatteryOptimizations ||
                                        workInBackgroundScreenState.isIgnoreBatteryOptimizationsRequestUnavailable)
                                ),
                modifier = Modifier.padding(horizontal = MaterialTheme.space.medium)
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(paddingValues = it)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Text(
                        text = stringResource(R.string.allow_optimal_work_in_background),
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
                        text = stringResource(R.string.allow_optimal_work_in_background_description),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(
                                start = MaterialTheme.space.medium,
                                end = MaterialTheme.space.medium,
                                bottom = MaterialTheme.space.medium
                            )
                    )

                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = MaterialTheme.space.xSmall
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.space.medium)
                            .clickable(
                                enabled = !workInBackgroundScreenState.isIgnoringBatteryOptimizations,
                                onClick = {
                                    try {
                                        context.startActivity(
                                            Intent(
                                                Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                                            ).apply {
                                                data = Uri.parse("package:${context.packageName}")
                                            }
                                        )
                                    } catch (exception: ActivityNotFoundException) {
                                        workInBackgroundViewModel.onEvent(
                                            WorkInBackgroundScreenEvent
                                                .IsIgnoreBatteryOptimizationsRequestUnavailableDialogVisible(
                                                    isVisible = true
                                                )
                                        )
                                    }
                                }
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(MaterialTheme.space.medium))

                            Icon(
                                imageVector = Icons.Outlined.SettingsSuggest,
                                contentDescription = stringResource(
                                    R.string.content_description_suggested_settings_icon
                                ),
                                modifier = Modifier.size(size = MaterialTheme.space.xLarge)
                            )

                            Text(
                                text = stringResource(
                                    if (!workInBackgroundScreenState.isIgnoringBatteryOptimizations) {
                                        R.string.work_in_background_limited_click_to_enable
                                    } else R.string.work_in_background_enabled
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
                                if (!workInBackgroundScreenState.isIgnoringBatteryOptimizations) {
                                    Icons.AutoMirrored.Outlined.NavigateNext
                                } else Icons.Outlined.Done,
                                contentDescription = stringResource(
                                    if (!workInBackgroundScreenState.isIgnoringBatteryOptimizations) {
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

                    Spacer(modifier = Modifier.height(MaterialTheme.space.medium))
                }

                Text(
                    text = stringResource(R.string.allow_full_work_in_background),
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
                    text = stringResource(
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                            R.string.allow_full_work_in_background_description_extended
                        } else {
                            R.string.allow_full_work_in_background_description
                        }
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.medium
                        )
                )

                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = MaterialTheme.space.xSmall
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.space.medium)
                        .clickable(
                            onClick = {
                                try {
                                    uriHandler.openUri(backgroundWorkImprovementWebsite)
                                    hasUserNavigatedToBackgroundWorkWebsite = true
                                } catch (exception: ActivityNotFoundException) {
                                    workInBackgroundViewModel.onEvent(
                                        WorkInBackgroundScreenEvent
                                            .IsWebBrowserNotFoundDialogVisible(isVisible = true)
                                    )
                                }
                            }
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(MaterialTheme.space.medium))

                        Icon(
                            imageVector = Icons.Outlined.AppSettingsAlt,
                            contentDescription = stringResource(
                                R.string.content_description_app_settings_icon
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
                            if (!hasUserFinishedBackgroundWorkInstructions ||
                                isLaunchedFromSettings
                            ) {
                                Icons.AutoMirrored.Outlined.NavigateNext
                            } else Icons.Outlined.Done,
                            contentDescription = stringResource(
                                if (!hasUserFinishedBackgroundWorkInstructions ||
                                    isLaunchedFromSettings
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
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
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
                                        if (workInBackgroundScreenState.hasUserTriedToGrantNotificationsPermission &&
                                            !permissionStatus.shouldShowRationale
                                        ) {
                                            workInBackgroundViewModel.onEvent(
                                                WorkInBackgroundScreenEvent
                                                    .IsNotificationsPermissionDialogVisible(
                                                        isVisible = true
                                                    )
                                            )
                                        } else {
                                            workInBackgroundViewModel.onEvent(
                                                WorkInBackgroundScreenEvent
                                                    .UserTriedToGrantNotificationsPermission
                                            )
                                            notificationsPermissionState.launchPermissionRequest()
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
                                    Icons.AutoMirrored.Outlined.NavigateNext
                                else Icons.Outlined.Done,
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
        }
    }

    if (workInBackgroundScreenState.isNotificationsPermissionDialogVisible) {
        Dialog(
            title = stringResource(R.string.notifications_permission_required),
            message = stringResource(R.string.notifications_permission_required_description),
            onDismissRequest = {
                workInBackgroundViewModel.onEvent(
                    WorkInBackgroundScreenEvent.IsNotificationsPermissionDialogVisible(
                        isVisible = false
                    )
                )
            },
            onPositiveClick = {
                workInBackgroundViewModel.onEvent(
                    WorkInBackgroundScreenEvent.IsNotificationsPermissionDialogVisible(
                        isVisible = false
                    )
                )
                context.startActivity(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:${context.packageName}")
                    }
                )
            },
            positiveButtonText = stringResource(R.string.settings),
            negativeButtonText = stringResource(R.string.later)
        )
    }

    if (workInBackgroundScreenState.isIgnoreBatteryOptimizationsRequestUnavailableDialogVisible) {
        Dialog(
            title = stringResource(R.string.setting_unavailable),
            message = stringResource(R.string.ignore_battery_optimization_unavailable),
            onDismissRequest = {
                workInBackgroundViewModel.onEvent(
                    WorkInBackgroundScreenEvent
                        .IsIgnoreBatteryOptimizationsRequestUnavailableDialogVisible(
                            isVisible = false
                        )
                )
            },
            onPositiveClick = {
                workInBackgroundViewModel.onEvent(
                    WorkInBackgroundScreenEvent
                        .IsIgnoreBatteryOptimizationsRequestUnavailableDialogVisible(
                            isVisible = false
                        )
                )
            },
            positiveButtonText = stringResource(R.string.ok)
        )
    }

    if (workInBackgroundScreenState.isWebBrowserNotFoundDialogVisible) {
        Dialog(
            title = stringResource(R.string.browser_not_found),
            message = stringResource(R.string.browser_not_found_open_link_manually),
            onDismissRequest = {
                workInBackgroundViewModel.onEvent(
                    WorkInBackgroundScreenEvent.IsWebBrowserNotFoundDialogVisible(isVisible = false)
                )
            },
            onPositiveClick = {
                hasUserFinishedBackgroundWorkInstructions = true
                workInBackgroundViewModel.onEvent(
                    WorkInBackgroundScreenEvent.IsWebBrowserNotFoundDialogVisible(isVisible = false)
                )
            },
            positiveButtonText = stringResource(R.string.ok)
        )
    }
}