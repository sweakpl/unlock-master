package com.sweak.unlockmaster.presentation.main.home

import android.content.Intent
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.components.Dialog
import com.sweak.unlockmaster.presentation.common.components.OnResume
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import com.sweak.unlockmaster.presentation.main.home.components.WeeklyUnlocksChart
import com.sweak.unlockmaster.presentation.unlock_counting.EXTRA_IS_UNLOCK_COUNTER_PAUSED
import com.sweak.unlockmaster.presentation.unlock_counting.UNLOCK_COUNTER_PAUSE_CHANGED

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {
    OnResume {
        homeViewModel.refresh()
    }

    val homeScreenState = homeViewModel.state

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.space.xxLarge)
                .background(color = MaterialTheme.colors.primary)
        ) {
            IconButton(onClick = { navController.navigate(Screen.SettingsScreen.route) }) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = stringResource(R.string.content_description_menu_icon)
                )
            }

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.h2
            )

            Spacer(
                modifier = Modifier
                    .size(
                        width = MaterialTheme.space.xLarge,
                        height = MaterialTheme.space.mediumLarge
                    )
            )
        }

        AnimatedContent(
            targetState = homeScreenState.isInitializing,
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) { isInitializing ->
            if (!isInitializing) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(
                                top = MaterialTheme.space.mediumLarge,
                                bottom = MaterialTheme.space.medium
                            )
                            .align(alignment = Alignment.CenterHorizontally)
                    ) {
                        val progressBarStrokeWidth = MaterialTheme.space.small
                        val progress: Float by animateFloatAsState(
                            targetValue = homeScreenState.run {
                                if (unlockCount == null || unlockLimit == null) 0f
                                else unlockCount.toFloat() / unlockLimit.toFloat()
                            }
                        )

                        CircularProgressIndicator(
                            progress = progress,
                            color = MaterialTheme.colors.primaryVariant,
                            strokeWidth = progressBarStrokeWidth,
                            modifier = Modifier
                                .size(size = 216.dp)
                                .padding(all = MaterialTheme.space.smallMedium)
                                .drawBehind {
                                    drawCircle(
                                        color = Color.White,
                                        radius = size.width / 2 - progressBarStrokeWidth.toPx() / 2,
                                        style = Stroke(
                                            width = progressBarStrokeWidth.toPx()
                                        ),
                                    )
                                }
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Text(
                                text = homeScreenState.unlockCount.toString(),
                                style = MaterialTheme.typography.h1.copy(fontSize = 48.sp)
                            )

                            Text(
                                text = stringResource(R.string.unlocks),
                                style = MaterialTheme.typography.h3
                            )
                        }

                        val context = LocalContext.current

                        IconButton(
                            onClick = {
                                homeViewModel.onEvent(
                                    HomeScreenEvent.TryPauseOrUnpauseUnlockCounter {
                                        context.sendBroadcast(
                                            Intent(UNLOCK_COUNTER_PAUSE_CHANGED).apply {
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

                    Card(
                        elevation = MaterialTheme.space.xSmall,
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
                                horizontalArrangement = Arrangement.SpaceBetween,
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
                                Column {
                                    Text(
                                        text = stringResource(R.string.todays_unlock_limit),
                                        style = MaterialTheme.typography.h4
                                    )

                                    Text(
                                        text = homeScreenState.unlockLimit.toString(),
                                        style = MaterialTheme.typography.h2
                                    )
                                }

                                Button(
                                    onClick = {
                                        navController.navigate(
                                            Screen.UnlockLimitSetupScreen.withArguments(
                                                true.toString()
                                            )
                                        )
                                    }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = stringResource(R.string.set_new),
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

                            AnimatedVisibility(
                                visible = homeScreenState.unlockLimitForTomorrow != null,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Card(
                                    backgroundColor = Color.Transparent,
                                    border = BorderStroke(
                                        width = 2.dp,
                                        color = MaterialTheme.colors.secondary
                                    ),
                                    elevation = MaterialTheme.space.default,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            navController.navigate(
                                                Screen.UnlockLimitSetupScreen.withArguments(
                                                    true.toString()
                                                )
                                            )
                                        }
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
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
                                            style = MaterialTheme.typography.subtitle2,
                                            modifier = Modifier
                                                .padding(end = MaterialTheme.space.small)
                                        )

                                        Text(
                                            text =
                                            homeScreenState.unlockLimitForTomorrow?.toString()
                                                ?: "",
                                            style = MaterialTheme.typography.h2
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Card(
                        elevation = MaterialTheme.space.xSmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = MaterialTheme.space.medium,
                                end = MaterialTheme.space.medium,
                                bottom = MaterialTheme.space.small
                            )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = MaterialTheme.space.medium)
                        ) {
                            Column {
                                Text(
                                    text = stringResource(R.string.todays_screen_time),
                                    style = MaterialTheme.typography.h4
                                )

                                Text(
                                    text = stringResource(
                                        R.string.hours_and_minutes_amount,
                                        homeScreenState.todayHoursAndMinutesScreenTimePair.first,
                                        homeScreenState.todayHoursAndMinutesScreenTimePair.second
                                    ),
                                    style = MaterialTheme.typography.h2
                                )
                            }

                            Button(
                                onClick = {
                                    navController.navigate(
                                        Screen.ScreenTimeScreen.withArguments(
                                            System.currentTimeMillis().toString()
                                        )
                                    )
                                }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(R.string.details),
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
                                .padding(all = MaterialTheme.space.medium)
                        ) {
                            Text(
                                text = stringResource(R.string.statistics_colon),
                                style = MaterialTheme.typography.h3,
                                modifier = Modifier.padding(bottom = MaterialTheme.space.xSmall)
                            )

                            Text(
                                text = stringResource(R.string.how_unlock_count_looked),
                                style = MaterialTheme.typography.subtitle1,
                                modifier = Modifier.padding(bottom = MaterialTheme.space.small)
                            )

                            WeeklyUnlocksChart(
                                lastWeekUnlockEventCountsEntries =
                                homeScreenState.lastWeekUnlockEventCounts,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(MaterialTheme.space.xxxLarge)
                                    .background(
                                        color = MaterialTheme.colors.background,
                                        shape = RoundedCornerShape(size = MaterialTheme.space.small)
                                    )
                            )

                            Button(
                                onClick = {
                                    navController.navigate(Screen.StatisticsScreen.route)
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

    val context = LocalContext.current

    if (homeScreenState.isUnlockCounterPauseConfirmationDialogVisible) {
        Dialog(
            title = stringResource(R.string.pause_counter),
            message = stringResource(R.string.pause_counter_description),
            onDismissRequest = {
                homeViewModel.onEvent(
                    HomeScreenEvent.UnlockCounterPauseConfirmationDialogVisibilityChanged(
                        isVisible = false
                    )
                )
            },
            onPositiveClick = {
                homeViewModel.onEvent(
                    HomeScreenEvent.PauseUnlockCounter {
                        context.sendBroadcast(
                            Intent(UNLOCK_COUNTER_PAUSE_CHANGED).apply {
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