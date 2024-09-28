package com.sweak.unlockmaster.presentation.main.statistics

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.NavigateNext
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.components.Dialog
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.components.OnResume
import com.sweak.unlockmaster.presentation.common.theme.space
import com.sweak.unlockmaster.presentation.common.util.Duration
import com.sweak.unlockmaster.presentation.common.util.getCompactDurationString
import com.sweak.unlockmaster.presentation.common.util.getFullDateString
import com.sweak.unlockmaster.presentation.common.util.navigateThrottled
import com.sweak.unlockmaster.presentation.common.util.popBackStackThrottled
import com.sweak.unlockmaster.presentation.main.statistics.components.AllTimeUnlocksChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    navController: NavController,
    statisticsViewModel: StatisticsViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    OnResume {
        statisticsViewModel.reload()
    }

    val statisticsScreenState = statisticsViewModel.state
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(
            connection = scrollBehavior.nestedScrollConnection
        ),
        topBar = {
            NavigationBar(
                title = stringResource(R.string.statistics),
                onNavigationButtonClick = { navController.popBackStackThrottled(lifecycleOwner) },
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        AnimatedContent(
            targetState = statisticsScreenState.isInitializing,
            contentAlignment = Alignment.Center,
            label = "statisticsScreenContentLoadingAnimation",
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) { isInitializing ->
            if (!isInitializing) {
                Column(modifier = Modifier.padding(paddingValues = paddingValues)) {
                    AllTimeUnlocksChart(
                        allTimeUnlockEventCountsEntries =
                        statisticsScreenState.allTimeUnlockEventCounts,
                        onValueSelected = {
                            statisticsViewModel.onEvent(
                                StatisticsScreenEvent.SelectChartValue(selectedEntryIndex = it)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(164.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(
                                horizontal = MaterialTheme.space.medium,
                                vertical = MaterialTheme.space.mediumLarge
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarMonth,
                            contentDescription = stringResource(R.string.content_description_calendar_icon),
                            modifier = Modifier.size(size = MaterialTheme.space.mediumLarge)
                        )

                        Text(
                            text = getFullDateString(
                                statisticsScreenState.currentlyHighlightedDayTimeInMillis
                            ),
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(start = MaterialTheme.space.medium)
                        )
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
                                .padding(
                                    horizontal = MaterialTheme.space.medium,
                                    vertical = MaterialTheme.space.mediumLarge
                                )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(bottom = MaterialTheme.space.mediumLarge)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.LockOpen,
                                    contentDescription = stringResource(
                                        R.string.content_description_open_padlock_icon
                                    ),
                                    modifier = Modifier.size(size = MaterialTheme.space.mediumLarge)
                                )

                                Text(
                                    text = stringResource(R.string.screen_unlocks),
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier
                                        .padding(horizontal = MaterialTheme.space.smallMedium)
                                        .weight(1f)
                                )

                                Text(
                                    text = statisticsScreenState.unlockEventsCount.toString(),
                                    style = MaterialTheme.typography.displayLarge
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(bottom = MaterialTheme.space.mediumLarge)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.MyLocation,
                                    contentDescription = stringResource(
                                        R.string.content_description_crosshair_icon
                                    ),
                                    modifier = Modifier
                                        .size(size = MaterialTheme.space.mediumLarge)
                                )

                                Text(
                                    text = stringResource(R.string.unlock_limit),
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier
                                        .padding(horizontal = MaterialTheme.space.smallMedium)
                                        .weight(1f)
                                )

                                Text(
                                    text = statisticsScreenState.unlockLimitAmount.toString(),
                                    style = MaterialTheme.typography.displayLarge
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(bottom = MaterialTheme.space.mediumLarge)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.LightMode,
                                    contentDescription = stringResource(
                                        R.string.content_description_light_icon
                                    ),
                                    modifier = Modifier.size(size = MaterialTheme.space.mediumLarge)
                                )

                                Text(
                                    text = stringResource(R.string.screen_on_events),
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier
                                        .padding(start = MaterialTheme.space.smallMedium)
                                )

                                CompositionLocalProvider(
                                    LocalMinimumInteractiveComponentSize provides Dp.Unspecified
                                ) {
                                    IconButton(
                                        onClick = {
                                            statisticsViewModel.onEvent(
                                                StatisticsScreenEvent
                                                    .ScreenOnEventsInformationDialogVisible(
                                                        isVisible = true
                                                    )
                                            )
                                        },
                                        modifier = Modifier
                                            .padding(
                                                start = MaterialTheme.space.small,
                                                end = MaterialTheme.space.smallMedium
                                            )
                                            .size(size = MaterialTheme.space.smallMedium)
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                                            contentDescription = stringResource(
                                                R.string.content_description_help_icon
                                            ),
                                            modifier = Modifier
                                                .size(size = MaterialTheme.space.smallMedium)
                                        )
                                    }
                                }

                                Text(
                                    text = statisticsScreenState.screenOnEventsCount.toString(),
                                    style = MaterialTheme.typography.displayLarge,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(bottom = MaterialTheme.space.mediumLarge)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.AccessTime,
                                    contentDescription = stringResource(
                                        R.string.content_description_clock_icon
                                    ),
                                    modifier = Modifier.size(size = MaterialTheme.space.mediumLarge)
                                )

                                Text(
                                    text = stringResource(R.string.screen_time),
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier
                                        .padding(horizontal = MaterialTheme.space.smallMedium)
                                        .weight(1f)
                                )

                                statisticsScreenState.screenTimeDurationMillis?.let {
                                    Text(
                                        text = getCompactDurationString(
                                            Duration(it, Duration.DisplayPrecision.MINUTES)
                                        ),
                                        style = MaterialTheme.typography.displayLarge
                                    )
                                }
                            }

                            if (statisticsScreenState.screenTimeLimitDurationMillis != null) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(bottom = MaterialTheme.space.mediumLarge)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Alarm,
                                        contentDescription = stringResource(
                                            R.string.content_description_alarm_icon
                                        ),
                                        modifier = Modifier
                                            .size(size = MaterialTheme.space.mediumLarge)
                                    )

                                    Text(
                                        text = stringResource(R.string.screen_time_limit),
                                        style = MaterialTheme.typography.headlineMedium,
                                        modifier = Modifier
                                            .padding(horizontal = MaterialTheme.space.smallMedium)
                                            .weight(1f)
                                    )

                                    Text(
                                        text = getCompactDurationString(
                                            Duration(
                                                statisticsScreenState.screenTimeLimitDurationMillis,
                                                Duration.DisplayPrecision.MINUTES
                                            )
                                        ),
                                        style = MaterialTheme.typography.displayLarge
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    navController.navigateThrottled(
                                        Screen.ScreenTimeScreen.withArguments(
                                            statisticsScreenState
                                                .currentlyHighlightedDayTimeInMillis.toString()
                                        ),
                                        lifecycleOwner
                                    )
                                },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(R.string.screen_time_details),
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

    if (statisticsScreenState.isScreenOnEventsInformationDialogVisible) {
        Dialog(
            title = stringResource(R.string.screen_on_events),
            message = stringResource(R.string.screen_on_event_description),
            onDismissRequest = {
                statisticsViewModel.onEvent(
                    StatisticsScreenEvent.ScreenOnEventsInformationDialogVisible(isVisible = false)
                )
            },
            onPositiveClick = {
                statisticsViewModel.onEvent(
                    StatisticsScreenEvent.ScreenOnEventsInformationDialogVisible(isVisible = false)
                )
            },
            positiveButtonText = stringResource(R.string.ok)
        )
    }
}