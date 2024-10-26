package com.sweak.unlockmaster.presentation.settings.application_blocked

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
import androidx.compose.material.icons.automirrored.outlined.TrendingDown
import androidx.compose.material.icons.outlined.AppSettingsAlt
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.MoodBad
import androidx.compose.material.icons.outlined.NoEncryption
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
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.components.Dialog
import com.sweak.unlockmaster.presentation.common.components.InformationCard
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.components.OnResume
import com.sweak.unlockmaster.presentation.common.components.ProceedButton
import com.sweak.unlockmaster.presentation.common.theme.space
import com.sweak.unlockmaster.presentation.common.util.popBackStackThrottled

@SuppressLint("BatteryLife")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationBlockedScreen(
    navController: NavController,
    applicationBlockedViewModel: ApplicationBlockedViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val applicationBlockedScreenState = applicationBlockedViewModel.state

    var hasUserNavigatedToBackgroundWorkWebsite by remember { mutableStateOf(false) }
    var hasUserFinishedBackgroundWorkInstructions by remember { mutableStateOf(false) }

    OnResume {
        if (hasUserNavigatedToBackgroundWorkWebsite) {
            hasUserFinishedBackgroundWorkInstructions = true
        }

        applicationBlockedViewModel.onEvent(
            ApplicationBlockedScreenEvent.CheckIfIgnoringBatteryOptimizations
        )
    }

    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val backgroundWorkImprovementWebsite = stringResource(R.string.dontkilmyapp_com_full_uri)

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(
            connection = scrollBehavior.nestedScrollConnection
        ),
        topBar = {
            NavigationBar(
                title = stringResource(R.string.application_blocked),
                onNavigationButtonClick = { navController.popBackStackThrottled(lifecycleOwner) },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ProceedButton(
                text = stringResource(R.string._continue),
                onClick = { navController.popBackStackThrottled(lifecycleOwner) },
                enabled = hasUserFinishedBackgroundWorkInstructions &&
                        (applicationBlockedScreenState.isIgnoringBatteryOptimizations ||
                                applicationBlockedScreenState.isIgnoreBatteryOptimizationsRequestUnavailable),
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
                Text(
                    text = stringResource(R.string.unlock_master_was_blocked_by_android),
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
                        R.string.unlock_master_was_blocked_by_android_description
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.mediumLarge
                        )
                )

                InformationCard(
                    title = stringResource(R.string.no_unlock_tracking),
                    description = stringResource(R.string.no_unlock_tracking_description),
                    icon = Icons.Outlined.NoEncryption,
                    iconContentDescription = stringResource(
                        R.string.content_description_no_unlock_tracking_icon
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.medium
                        )
                )

                InformationCard(
                    title = stringResource(R.string.missing_data),
                    description = stringResource(R.string.missing_data_description),
                    icon = Icons.AutoMirrored.Outlined.TrendingDown,
                    iconContentDescription = stringResource(
                        R.string.content_description_missing_data_icon
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.medium
                        )
                )

                InformationCard(
                    title = stringResource(R.string.degraded_experience),
                    description = stringResource(R.string.degraded_experience_description),
                    icon = Icons.Outlined.MoodBad,
                    iconContentDescription = stringResource(
                        R.string.content_description_bad_mood_icon
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.large
                        )
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Text(
                        text = stringResource(R.string.allow_optimal_work_in_background),
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier
                            .padding(
                                start = MaterialTheme.space.medium,
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
                                enabled = !applicationBlockedScreenState.isIgnoringBatteryOptimizations,
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
                                        applicationBlockedViewModel.onEvent(
                                            ApplicationBlockedScreenEvent
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
                                    if (!applicationBlockedScreenState.isIgnoringBatteryOptimizations) {
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
                                if (!applicationBlockedScreenState.isIgnoringBatteryOptimizations) {
                                    Icons.AutoMirrored.Outlined.NavigateNext
                                } else Icons.Outlined.Done,
                                contentDescription = stringResource(
                                    if (!applicationBlockedScreenState.isIgnoringBatteryOptimizations) {
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

                    Spacer(modifier = Modifier.height(MaterialTheme.space.large))
                }

                Text(
                    text = stringResource(R.string.allow_full_work_in_background),
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.small
                        )
                )

                Text(
                    text = stringResource(R.string.allow_full_work_in_background_description),
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
                                    applicationBlockedViewModel.onEvent(
                                        ApplicationBlockedScreenEvent
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
                            if (!hasUserFinishedBackgroundWorkInstructions) {
                                Icons.AutoMirrored.Outlined.NavigateNext
                            } else Icons.Outlined.Done,
                            contentDescription = stringResource(
                                if (!hasUserFinishedBackgroundWorkInstructions) {
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

                Spacer(modifier = Modifier.height(MaterialTheme.space.run { xLarge + 2 * medium }))
            }
        }
    }

    if (applicationBlockedScreenState.isIgnoreBatteryOptimizationsRequestUnavailableDialogVisible) {
        Dialog(
            title = stringResource(R.string.setting_unavailable),
            message = stringResource(R.string.ignore_battery_optimization_unavailable),
            onDismissRequest = {
                applicationBlockedViewModel.onEvent(
                    ApplicationBlockedScreenEvent
                        .IsIgnoreBatteryOptimizationsRequestUnavailableDialogVisible(
                            isVisible = false
                        )
                )
            },
            onPositiveClick = {
                applicationBlockedViewModel.onEvent(
                    ApplicationBlockedScreenEvent
                        .IsIgnoreBatteryOptimizationsRequestUnavailableDialogVisible(
                            isVisible = false
                        )
                )
            },
            positiveButtonText = stringResource(R.string.ok)
        )
    }

    if (applicationBlockedScreenState.isWebBrowserNotFoundDialogVisible) {
        Dialog(
            title = stringResource(R.string.browser_not_found),
            message = stringResource(R.string.browser_not_found_open_link_manually),
            onDismissRequest = {
                applicationBlockedViewModel.onEvent(
                    ApplicationBlockedScreenEvent
                        .IsWebBrowserNotFoundDialogVisible(isVisible = false)
                )
            },
            onPositiveClick = {
                hasUserFinishedBackgroundWorkInstructions = true
                applicationBlockedViewModel.onEvent(
                    ApplicationBlockedScreenEvent
                        .IsWebBrowserNotFoundDialogVisible(isVisible = false)
                )
            },
            positiveButtonText = stringResource(R.string.ok)
        )
    }
}