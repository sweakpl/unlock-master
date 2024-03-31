package com.sweak.unlockmaster.presentation.introduction.limit_setup.screen_time

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.ModeEdit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.background_work.ACTION_SCREEN_TIME_LIMIT_STATE_CHANGED
import com.sweak.unlockmaster.presentation.background_work.EXTRA_IS_SCREEN_TIME_LIMIT_ENABLED
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.components.Dialog
import com.sweak.unlockmaster.presentation.common.components.InformationCard
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.components.ObserveAsEvents
import com.sweak.unlockmaster.presentation.common.components.ProceedButton
import com.sweak.unlockmaster.presentation.common.theme.space
import com.sweak.unlockmaster.presentation.common.util.Duration
import com.sweak.unlockmaster.presentation.common.util.getCompactDurationString
import com.sweak.unlockmaster.presentation.common.util.navigateThrottled
import com.sweak.unlockmaster.presentation.common.util.popBackStackThrottled
import com.sweak.unlockmaster.presentation.introduction.components.ScreenTimeLimitPickerSlider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTimeLimitSetupScreen(
    screenTimeLimitSetupViewModel: ScreenTimeLimitSetupViewModel = hiltViewModel(),
    navController: NavController,
    isUpdatingExistingScreenTimeLimit: Boolean
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    ObserveAsEvents(
        flow = screenTimeLimitSetupViewModel.screenTimeLimitSubmittedEvents,
        onEvent = {
            if (isUpdatingExistingScreenTimeLimit) {
                navController.popBackStackThrottled(lifecycleOwner)
            } else {
                navController.navigateThrottled(
                    Screen.WorkInBackgroundScreen.withArguments(false.toString()),
                    lifecycleOwner
                )
            }
        }
    )

    val screenTimeLimitSetupScreenState = screenTimeLimitSetupViewModel.state
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    BackHandler(enabled = screenTimeLimitSetupScreenState.hasUserChangedAnySettings) {
        screenTimeLimitSetupViewModel.onEvent(
            ScreenTimeLimitSetupScreenEvent.IsSettingsNotSavedDialogVisible(isVisible = true)
        )
    }

    Scaffold(
        modifier = Modifier.nestedScroll(
            connection = scrollBehavior.nestedScrollConnection
        ),
        topBar = {
            NavigationBar(
                title = stringResource(R.string.screen_time_limit),
                onNavigationButtonClick = {
                    if (screenTimeLimitSetupScreenState.hasUserChangedAnySettings) {
                        screenTimeLimitSetupViewModel.onEvent(
                            ScreenTimeLimitSetupScreenEvent.IsSettingsNotSavedDialogVisible(
                                isVisible = true
                            )
                        )
                    } else {
                        navController.popBackStackThrottled(lifecycleOwner)
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ProceedButton(
                text = stringResource(R.string.confirm),
                onClick = {
                    screenTimeLimitSetupViewModel.onEvent(
                        ScreenTimeLimitSetupScreenEvent.SubmitSelectedScreenTimeLimit
                    )
                },
                modifier = Modifier.padding(horizontal = MaterialTheme.space.medium)
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = it)
        ) {
            Column(
                modifier = Modifier
                    .matchParentSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(R.string.set_up_screen_time_limit),
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
                    text = stringResource(R.string.set_up_screen_time_limit_description),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.medium
                        )
                )

                if (screenTimeLimitSetupScreenState.pickedScreenTimeLimitMinutes != null &&
                    screenTimeLimitSetupScreenState.availableScreenTimeLimitRange != null &&
                    screenTimeLimitSetupScreenState.screenTimeLimitIntervalMinutes != null
                ) {
                    ScreenTimeLimitPickerSlider(
                        pickedScreenTimeMinutes =
                        screenTimeLimitSetupScreenState.pickedScreenTimeLimitMinutes,
                        limitRange = screenTimeLimitSetupScreenState.availableScreenTimeLimitRange,
                        stepIntervalMinutes =
                        screenTimeLimitSetupScreenState.screenTimeLimitIntervalMinutes,
                        enabled = screenTimeLimitSetupScreenState.isScreenTimeLimitEnabled,
                        onNewScreenTimeMinutesPicked = { newScreenTimeLimitMinutes ->
                            screenTimeLimitSetupViewModel.onEvent(
                                ScreenTimeLimitSetupScreenEvent.PickNewScreenTimeLimit(
                                    newScreenTimeLimitMinutes = newScreenTimeLimitMinutes
                                )
                            )
                        },
                        modifier = Modifier
                            .padding(
                                start = MaterialTheme.space.medium,
                                end = MaterialTheme.space.medium,
                                bottom = MaterialTheme.space.large,
                            )
                    )
                }

                AnimatedVisibility(
                    visible = screenTimeLimitSetupScreenState.isScreenTimeLimitEnabled &&
                            screenTimeLimitSetupScreenState.screenTimeLimitMinutesForTomorrow != null
                ) {
                    OutlinedCard(
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = Color.Transparent
                        ),
                        border = BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.tertiary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = MaterialTheme.space.medium,
                                end = MaterialTheme.space.medium,
                                bottom = MaterialTheme.space.medium
                            )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(
                                horizontal = MaterialTheme.space.smallMedium,
                                vertical = MaterialTheme.space.small
                            )
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(
                                        R.string.new_screen_time_limit_set_for_tomorrow
                                    ),
                                    style = MaterialTheme.typography.titleSmall
                                )

                                screenTimeLimitSetupScreenState.screenTimeLimitMinutesForTomorrow
                                    ?.let { screenTimeLimitMinutesForTomorrow ->
                                        Text(
                                            text = getCompactDurationString(
                                                Duration(
                                                    durationMillis =
                                                    screenTimeLimitMinutesForTomorrow * 60000L,
                                                    precision = Duration.DisplayPrecision.MINUTES
                                                )
                                            ),
                                            style = MaterialTheme.typography.displayMedium
                                        )
                                    }
                            }

                            IconButton(
                                onClick = {
                                    screenTimeLimitSetupViewModel.onEvent(
                                        ScreenTimeLimitSetupScreenEvent
                                            .IsRemoveScreenTimeLimitForTomorrowDialogVisible(
                                                isVisible = true
                                            )
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = stringResource(
                                        R.string.content_description_delete_icon
                                    ),
                                    modifier = Modifier.size(size = MaterialTheme.space.mediumLarge)
                                )
                            }
                        }
                    }
                }

                ElevatedCard(
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = MaterialTheme.space.xSmall
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.medium,
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
                                text = stringResource(R.string.enable_screen_time_limit),
                                style = MaterialTheme.typography.displaySmall
                            )

                            Text(
                                text = stringResource(R.string.enable_screen_time_limit_description),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        val context = LocalContext.current

                        Switch(
                            checked = screenTimeLimitSetupScreenState.isScreenTimeLimitEnabled,
                            onCheckedChange = { isChecked ->
                                screenTimeLimitSetupViewModel.onEvent(
                                    ScreenTimeLimitSetupScreenEvent
                                        .ToggleScreenTimeLimitState(
                                            isEnabled = isChecked,
                                            screenTimeLimitStateChangedCallback = {
                                                context.sendBroadcast(
                                                    Intent(ACTION_SCREEN_TIME_LIMIT_STATE_CHANGED)
                                                        .apply {
                                                            setPackage(context.packageName)
                                                            putExtra(
                                                                EXTRA_IS_SCREEN_TIME_LIMIT_ENABLED,
                                                                it
                                                            )
                                                        }
                                                )
                                            }
                                        )
                                )
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.secondary,
                                checkedTrackColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.space.large))

                Text(
                    text = stringResource(R.string.screen_time_limit_purposes),
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(
                        start = MaterialTheme.space.medium,
                        end = MaterialTheme.space.medium,
                        bottom = MaterialTheme.space.medium,
                    )
                )

                InformationCard(
                    title = stringResource(R.string.complementation),
                    description = stringResource(R.string.complementation_description),
                    icon = Icons.Outlined.Layers,
                    iconContentDescription = stringResource(
                        R.string.content_description_complementing_layers_icon
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
                    title = stringResource(R.string.adjustability),
                    description = stringResource(
                        R.string.adjustability_description_screen_time_limit
                    ),
                    icon = Icons.Outlined.ModeEdit,
                    iconContentDescription = stringResource(
                        R.string.content_description_edit_icon
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.mediumLarge,
                        )
                )

                Text(
                    text = stringResource(R.string.screen_time_limit_warnings),
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.small
                        )
                )

                Text(
                    text = stringResource(R.string.screen_time_limit_warnings_description),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.medium
                        )
                )

                Image(
                    painter = painterResource(R.drawable.img_screen_time_mobilizing_notification),
                    contentDescription = stringResource(
                        R.string.content_description_screen_time_limit_mobilizing_notification_image
                    ),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.space.mediumLarge)
                )

                Spacer(modifier = Modifier.height(MaterialTheme.space.run { xLarge + 2 * medium }))
            }
        }
    }

    if (screenTimeLimitSetupScreenState.isRemoveScreenTimeLimitForTomorrowDialogVisible) {
        Dialog(
            title = stringResource(R.string.remove_tomorrow_screen_time_limit),
            message = stringResource(R.string.remove_tomorrow_screen_time_limit_description),
            onDismissRequest = {
                screenTimeLimitSetupViewModel.onEvent(
                    ScreenTimeLimitSetupScreenEvent.IsRemoveScreenTimeLimitForTomorrowDialogVisible(
                        isVisible = false
                    )
                )
            },
            onPositiveClick = {
                screenTimeLimitSetupViewModel.onEvent(
                    ScreenTimeLimitSetupScreenEvent.ConfirmRemoveScreenTimeLimitForTomorrow
                )
            },
            positiveButtonText = stringResource(R.string.yes),
            negativeButtonText = stringResource(R.string.no)
        )
    }

    if (screenTimeLimitSetupScreenState.isSettingsNotSavedDialogVisible) {
        Dialog(
            title = stringResource(R.string.setting_not_saved),
            message = stringResource(R.string.setting_not_saved_description),
            onDismissRequest = {
                screenTimeLimitSetupViewModel.onEvent(
                    ScreenTimeLimitSetupScreenEvent.IsSettingsNotSavedDialogVisible(
                        isVisible = false
                    )
                )
                navController.popBackStackThrottled(lifecycleOwner)
            },
            onPositiveClick = {
                screenTimeLimitSetupViewModel.onEvent(
                    ScreenTimeLimitSetupScreenEvent.IsSettingsNotSavedDialogVisible(
                        isVisible = false
                    )
                )
            },
            positiveButtonText = stringResource(R.string.ok),
            negativeButtonText = stringResource(R.string.exit)
        )
    }
}
