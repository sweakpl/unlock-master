package com.sweak.unlockmaster.presentation.main.home

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.NavigateNext
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.AppBlocking
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.background_work.ACTION_UNLOCK_COUNTER_PAUSE_CHANGED
import com.sweak.unlockmaster.presentation.background_work.EXTRA_IS_UNLOCK_COUNTER_PAUSED
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.components.Dialog
import com.sweak.unlockmaster.presentation.common.components.OnResume
import com.sweak.unlockmaster.presentation.common.theme.space
import com.sweak.unlockmaster.presentation.common.util.Duration
import com.sweak.unlockmaster.presentation.common.util.getCompactDurationString
import com.sweak.unlockmaster.presentation.common.util.navigateThrottled
import com.sweak.unlockmaster.presentation.main.home.components.WeeklyUnlocksChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navController: NavController,
    onBack: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    OnResume {
        homeViewModel.refresh()
    }

    val homeScreenState = homeViewModel.state
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    BackHandler(enabled = homeScreenState.shouldShowUnlockMasterBlockedWarning) {
        homeViewModel.onEvent(HomeScreenEvent.DismissUnlockMasterBlockedWarning)
        onBack()
    }

    Scaffold(
        modifier = Modifier.nestedScroll(
            connection = scrollBehavior.nestedScrollConnection
        ),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.displayMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateThrottled(
                                Screen.SettingsScreen.route,
                                lifecycleOwner
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Menu,
                            contentDescription = stringResource(
                                R.string.content_description_menu_icon
                            )
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        AnimatedContent(
            targetState = homeScreenState.isInitializing,
            contentAlignment = Alignment.Center,
            label = "homeScreenContentLoadingAnimation",
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) { isInitializing ->
            if (!isInitializing) {
                Column(modifier = Modifier.padding(paddingValues = paddingValues)) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(
                                top = MaterialTheme.space.mediumLarge,
                                bottom = MaterialTheme.space.medium
                            )
                            .align(alignment = Alignment.CenterHorizontally)
                    ) {
                        val progress: Float by animateFloatAsState(
                            targetValue = homeScreenState.run {
                                if (unlockCount == null || unlockLimit == null) 0f
                                else unlockCount.toFloat() / unlockLimit.toFloat()
                            },
                            label = "unlockLimitProgressAnimation"
                        )

                        CircularProgressIndicator(
                            progress = { progress },
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surface,
                            strokeWidth = MaterialTheme.space.small,
                            modifier = Modifier
                                .size(size = 216.dp)
                                .padding(all = MaterialTheme.space.smallMedium)
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Text(
                                text = homeScreenState.unlockCount.toString(),
                                style = MaterialTheme.typography.displayLarge.copy(fontSize = 48.sp)
                            )

                            Text(
                                text = pluralStringResource(
                                    R.plurals.unlocks,
                                    homeScreenState.unlockCount ?: 0
                                ),
                                style = MaterialTheme.typography.displaySmall
                            )
                        }

                        val context = LocalContext.current

                        IconButton(
                            onClick = {
                                homeViewModel.onEvent(
                                    HomeScreenEvent.TryPauseOrUnpauseUnlockCounter {
                                        context.sendBroadcast(
                                            Intent(ACTION_UNLOCK_COUNTER_PAUSE_CHANGED).apply {
                                                setPackage(context.packageName)
                                                putExtra(EXTRA_IS_UNLOCK_COUNTER_PAUSED, it)
                                            }
                                        )
                                    }
                                )
                            },
                            modifier = Modifier.align(Alignment.TopStart)
                        ) {
                            homeScreenState.isUnlockCounterPaused?.let {
                                Icon(
                                    imageVector =
                                    if (it) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                                    contentDescription =
                                    if (it) stringResource(R.string.content_description_play_icon)
                                    else stringResource(R.string.content_description_pause_icon)
                                )
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = homeScreenState.shouldShowUnlockMasterBlockedWarning,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ElevatedCard(
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.error
                            ),
                            elevation = CardDefaults.elevatedCardElevation(
                                defaultElevation = MaterialTheme.space.xSmall
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = MaterialTheme.space.medium,
                                    end = MaterialTheme.space.medium,
                                    bottom = MaterialTheme.space.mediumLarge
                                )
                                .clickable(
                                    onClick = {
                                        homeViewModel.onEvent(
                                            HomeScreenEvent.DismissUnlockMasterBlockedWarning
                                        )
                                        navController.navigateThrottled(
                                            Screen.ApplicationBlockedScreen.route,
                                            lifecycleOwner
                                        )
                                    }
                                )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.width(MaterialTheme.space.medium))

                                Icon(
                                    imageVector = Icons.Outlined.AppBlocking,
                                    contentDescription = stringResource(
                                        R.string.content_description_app_blocked_icon
                                    ),
                                    modifier = Modifier.size(size = MaterialTheme.space.xLarge)
                                )

                                Text(
                                    text = stringResource(R.string.unlock_master_was_blocked_click_here),
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier
                                        .padding(all = MaterialTheme.space.medium)
                                        .weight(1f)
                                )

                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.NavigateNext,
                                    contentDescription = stringResource(
                                        R.string.content_description_next_icon
                                    ),
                                    modifier = Modifier
                                        .size(size = MaterialTheme.space.xLarge)
                                        .padding(all = MaterialTheme.space.small)
                                )

                                Spacer(modifier = Modifier.width(MaterialTheme.space.small))
                            }
                        }
                    }

                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = MaterialTheme.space.xSmall
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = MaterialTheme.space.medium,
                                end = MaterialTheme.space.medium,
                                bottom = MaterialTheme.space.small
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = MaterialTheme.space.medium)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(
                                    MaterialTheme.space.medium
                                ),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        bottom =
                                        if (homeScreenState.unlockLimitForTomorrow != null)
                                            MaterialTheme.space.smallMedium
                                        else MaterialTheme.space.default
                                    )
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = stringResource(R.string.todays_unlock_limit),
                                        style = MaterialTheme.typography.headlineMedium
                                    )

                                    Text(
                                        text = homeScreenState.unlockLimit.toString(),
                                        style = MaterialTheme.typography.displayMedium
                                    )
                                }

                                Button(
                                    onClick = {
                                        navController.navigateThrottled(
                                            Screen.UnlockLimitSetupScreen.withArguments(
                                                true.toString()
                                            ),
                                            lifecycleOwner
                                        )
                                    }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = stringResource(R.string.set_new),
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier
                                                .padding(end = MaterialTheme.space.small)
                                        )

                                        Icon(
                                            imageVector = Icons.AutoMirrored.Outlined.NavigateNext,
                                            contentDescription = stringResource(
                                                R.string.content_description_next_icon
                                            )
                                        )
                                    }
                                }
                            }

                            AnimatedVisibility(
                                visible = homeScreenState.unlockLimitForTomorrow != null,
                                modifier = Modifier.fillMaxWidth()
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
                                        .clickable {
                                            navController.navigateThrottled(
                                                Screen.UnlockLimitSetupScreen.withArguments(
                                                    true.toString()
                                                ),
                                                lifecycleOwner
                                            )
                                        }
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(
                                            MaterialTheme.space.medium
                                        ),
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(
                                            horizontal = MaterialTheme.space.smallMedium,
                                            vertical = MaterialTheme.space.small
                                        )
                                    ) {
                                        Text(
                                            text = stringResource(
                                                R.string.new_unlock_limit_set_for_tomorrow
                                            ),
                                            style = MaterialTheme.typography.titleSmall,
                                            modifier = Modifier.weight(1f)
                                        )

                                        Text(
                                            text =
                                            homeScreenState.unlockLimitForTomorrow?.toString()
                                                ?: "",
                                            style = MaterialTheme.typography.displayMedium
                                        )
                                    }
                                }
                            }
                        }
                    }

                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = MaterialTheme.space.xSmall
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = MaterialTheme.space.medium,
                                end = MaterialTheme.space.medium,
                                bottom = MaterialTheme.space.small
                            )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(
                                MaterialTheme.space.medium
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = MaterialTheme.space.medium)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.todays_screen_time),
                                    style = MaterialTheme.typography.headlineMedium
                                )

                                homeScreenState.todayScreenTimeDurationMillis?.let {
                                    Text(
                                        text = getCompactDurationString(
                                            Duration(it, Duration.DisplayPrecision.MINUTES)
                                        ),
                                        style = MaterialTheme.typography.displayMedium
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    navController.navigateThrottled(
                                        Screen.ScreenTimeScreen.withArguments(
                                            System.currentTimeMillis().toString()
                                        ),
                                        lifecycleOwner
                                    )
                                }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(R.string.details),
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier
                                            .padding(end = MaterialTheme.space.small)
                                    )

                                    Icon(
                                        imageVector = Icons.AutoMirrored.Outlined.NavigateNext,
                                        contentDescription = stringResource(
                                            R.string.content_description_next_icon
                                        )
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = homeScreenState.isScreenTimeLimitEnabled &&
                                    homeScreenState.screenTimeLimitMinutes != null
                        ) {
                            val minuteInMillis = 60000L
                            val progress: Float by animateFloatAsState(
                                targetValue = homeScreenState.run {
                                    if (todayScreenTimeDurationMillis == null ||
                                        screenTimeLimitMinutes == null
                                    ) {
                                        0f
                                    } else {
                                        val todayScreenTimeDurationMinutes =
                                            todayScreenTimeDurationMillis / minuteInMillis

                                        todayScreenTimeDurationMinutes.toFloat() /
                                                screenTimeLimitMinutes.toFloat()
                                    }
                                },
                                label = "screenTimeLimitProgressAnimation"
                            )

                            LinearProgressIndicator(
                                progress = { progress },
                                color = MaterialTheme.colorScheme.secondary,
                                trackColor = MaterialTheme.colorScheme.background,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = MaterialTheme.space.medium)
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = MaterialTheme.space.medium,
                                        top = MaterialTheme.space.mediumLarge,
                                        end = MaterialTheme.space.medium,
                                        bottom = MaterialTheme.space.medium
                                    )
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(
                                        MaterialTheme.space.medium
                                    ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            bottom =
                                            if (homeScreenState.screenTimeLimitForTomorrowMinutes != null)
                                                MaterialTheme.space.smallMedium
                                            else MaterialTheme.space.default
                                        )
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = stringResource(R.string.screen_time_limit_colon),
                                            style = MaterialTheme.typography.headlineMedium
                                        )

                                        homeScreenState.screenTimeLimitMinutes
                                            ?.let { screenTimeLimitMinutes ->
                                                Text(
                                                    text = getCompactDurationString(
                                                        Duration(
                                                            screenTimeLimitMinutes * minuteInMillis,
                                                            Duration.DisplayPrecision.MINUTES
                                                        )
                                                    ),
                                                    style = MaterialTheme.typography.displayMedium
                                                )
                                            }
                                    }

                                    Button(
                                        onClick = {
                                            navController.navigateThrottled(
                                                Screen.ScreenTimeLimitSetupScreen.withArguments(
                                                    true.toString()
                                                ),
                                                lifecycleOwner
                                            )
                                        }
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = stringResource(R.string.set_new),
                                                style = MaterialTheme.typography.titleMedium,
                                                modifier = Modifier
                                                    .padding(end = MaterialTheme.space.small)
                                            )

                                            Icon(
                                                imageVector = Icons.AutoMirrored.Outlined.NavigateNext,
                                                contentDescription = stringResource(
                                                    R.string.content_description_next_icon
                                                )
                                            )
                                        }
                                    }
                                }

                                AnimatedVisibility(
                                    visible =
                                    homeScreenState.screenTimeLimitForTomorrowMinutes != null,
                                    modifier = Modifier.fillMaxWidth()
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
                                            .clickable {
                                                navController.navigateThrottled(
                                                    Screen.ScreenTimeLimitSetupScreen.withArguments(
                                                        true.toString()
                                                    ),
                                                    lifecycleOwner
                                                )
                                            }
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(
                                                MaterialTheme.space.medium
                                            ),
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(
                                                horizontal = MaterialTheme.space.smallMedium,
                                                vertical = MaterialTheme.space.small
                                            )
                                        ) {
                                            Text(
                                                text = stringResource(
                                                    R.string.new_screen_time_limit_set_for_tomorrow
                                                ),
                                                style = MaterialTheme.typography.titleSmall,
                                                modifier = Modifier.weight(1f)
                                            )

                                            homeScreenState.screenTimeLimitForTomorrowMinutes
                                                ?.let { screenTimeLimitForTomorrowMinutes ->
                                                    Text(
                                                        text = getCompactDurationString(
                                                            Duration(
                                                                screenTimeLimitForTomorrowMinutes * minuteInMillis,
                                                                Duration.DisplayPrecision.MINUTES
                                                            )
                                                        ),
                                                        style = MaterialTheme.typography.displayMedium
                                                    )
                                                }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = MaterialTheme.space.xSmall
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = MaterialTheme.space.medium,
                                end = MaterialTheme.space.medium,
                                bottom = MaterialTheme.space.large
                            )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .padding(all = MaterialTheme.space.medium)
                        ) {
                            Text(
                                text = stringResource(R.string.statistics_colon),
                                style = MaterialTheme.typography.displaySmall,
                                modifier = Modifier.padding(bottom = MaterialTheme.space.xSmall)
                            )

                            Text(
                                text = stringResource(R.string.how_unlock_count_looked),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = MaterialTheme.space.small)
                            )

                            WeeklyUnlocksChart(
                                lastWeekUnlockEventCountsEntries =
                                homeScreenState.lastWeekUnlockEventCounts,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(MaterialTheme.space.xxxLarge)
                                    .background(
                                        color = MaterialTheme.colorScheme.background,
                                        shape = RoundedCornerShape(size = MaterialTheme.space.small)
                                    )
                            )

                            Button(
                                onClick = {
                                    navController.navigateThrottled(
                                        Screen.StatisticsScreen.route,
                                        lifecycleOwner
                                    )
                                },
                                modifier = Modifier
                                    .padding(top = MaterialTheme.space.medium)
                                    .align(Alignment.End)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(R.string.get_more_insights),
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier
                                            .padding(end = MaterialTheme.space.small)
                                    )

                                    Icon(
                                        imageVector = Icons.AutoMirrored.Outlined.NavigateNext,
                                        contentDescription = stringResource(
                                            R.string.content_description_next_icon
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(paddingValues = paddingValues)
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .size(MaterialTheme.space.xLarge)
                            .align(alignment = Alignment.Center)
                    )
                }
            }
        }
    }

    val context = LocalContext.current

    if (homeScreenState.isUnlockCounterPauseConfirmationDialogVisible) {
        Dialog(
            title = stringResource(R.string.pause_counter),
            message = stringResource(R.string.pause_counter_description),
            onDismissRequest = {
                homeViewModel.onEvent(
                    HomeScreenEvent.IsUnlockCounterPauseConfirmationDialogVisible(
                        isVisible = false
                    )
                )
            },
            onPositiveClick = {
                homeViewModel.onEvent(
                    HomeScreenEvent.PauseUnlockCounter {
                        context.sendBroadcast(
                            Intent(ACTION_UNLOCK_COUNTER_PAUSE_CHANGED).apply {
                                setPackage(context.packageName)
                                putExtra(EXTRA_IS_UNLOCK_COUNTER_PAUSED, it)
                            }
                        )
                    }
                )
            },
            positiveButtonText = stringResource(R.string.pause),
            negativeButtonText = stringResource(R.string.cancel)
        )
    }
}