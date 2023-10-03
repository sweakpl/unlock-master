package com.sweak.unlockmaster.presentation.main.statistics

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.components.Dialog
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.components.OnResume
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import com.sweak.unlockmaster.presentation.common.util.getCompactDurationString
import com.sweak.unlockmaster.presentation.common.util.getFullDateString
import com.sweak.unlockmaster.presentation.common.util.navigateThrottled
import com.sweak.unlockmaster.presentation.common.util.popBackStackThrottled
import com.sweak.unlockmaster.presentation.main.statistics.components.AllTimeUnlocksChart

@OptIn(ExperimentalMaterialApi::class)
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

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        NavigationBar(
            title = stringResource(R.string.statistics),
            onBackClick = { navController.popBackStackThrottled(lifecycleOwner) }
        )

        AnimatedContent(
            targetState = statisticsScreenState.isInitializing,
            contentAlignment = Alignment.Center,
            label = "statisticsScreenContentLoadingAnimation",
            modifier = Modifier.fillMaxWidth()
        ) { isInitializing ->
            if (!isInitializing) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
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
                            style = MaterialTheme.typography.h4,
                            modifier = Modifier.padding(start = MaterialTheme.space.medium)
                        )
                    }

                    Card(
                        elevation = MaterialTheme.space.xSmall,
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
                                    style = MaterialTheme.typography.h4,
                                    modifier = Modifier
                                        .padding(horizontal = MaterialTheme.space.smallMedium)
                                        .weight(1f)
                                )

                                Text(
                                    text = statisticsScreenState.unlockEventsCount.toString(),
                                    style = MaterialTheme.typography.h1
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
                                    style = MaterialTheme.typography.h4,
                                    modifier = Modifier
                                        .padding(horizontal = MaterialTheme.space.smallMedium)
                                        .weight(1f)
                                )

                                Text(
                                    text = statisticsScreenState.unlockLimitAmount.toString(),
                                    style = MaterialTheme.typography.h1
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
                                    style = MaterialTheme.typography.h4,
                                    modifier = Modifier
                                        .padding(start = MaterialTheme.space.smallMedium)
                                )

                                CompositionLocalProvider(
                                    LocalMinimumInteractiveComponentEnforcement provides false
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
                                        modifier = Modifier.padding(
                                            start = MaterialTheme.space.xSmall,
                                            end = MaterialTheme.space.smallMedium
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.HelpOutline,
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
                                    style = MaterialTheme.typography.h1,
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
                                    style = MaterialTheme.typography.h4,
                                    modifier = Modifier
                                        .padding(horizontal = MaterialTheme.space.smallMedium)
                                        .weight(1f)
                                )

                                statisticsScreenState.screenTimeDuration?.let {
                                    Text(
                                        text = getCompactDurationString(it),
                                        style = MaterialTheme.typography.h1
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
                                        style = MaterialTheme.typography.subtitle1,
                                        modifier = Modifier
                                            .padding(end = MaterialTheme.space.small)
                                    )

                                    Icon(
                                        imageVector = Icons.Outlined.NavigateNext,
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
                Box(modifier = Modifier.fillMaxHeight()) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.primaryVariant,
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