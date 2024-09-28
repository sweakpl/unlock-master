package com.sweak.unlockmaster.presentation.main.screen_time

import android.text.format.DateFormat
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.components.OnResume
import com.sweak.unlockmaster.presentation.common.theme.space
import com.sweak.unlockmaster.presentation.common.util.Duration
import com.sweak.unlockmaster.presentation.common.util.TimeFormat
import com.sweak.unlockmaster.presentation.common.util.getCompactDurationString
import com.sweak.unlockmaster.presentation.common.util.getFullDateString
import com.sweak.unlockmaster.presentation.common.util.popBackStackThrottled
import com.sweak.unlockmaster.presentation.main.screen_time.components.CounterPauseSeparator
import com.sweak.unlockmaster.presentation.main.screen_time.components.DailyScreenTimeChart
import com.sweak.unlockmaster.presentation.main.screen_time.components.SingleScreenTimeSessionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTimeScreen(
    screenTimeViewModel: ScreenTimeViewModel = hiltViewModel(),
    navController: NavController,
    displayedScreenTimeDayTimeInMillis: Long
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    OnResume {
        screenTimeViewModel.refresh()
    }

    val screenTimeScreenState = screenTimeViewModel.state
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(
            connection = scrollBehavior.nestedScrollConnection
        ),
        topBar = {
            NavigationBar(
                title = getFullDateString(displayedScreenTimeDayTimeInMillis),
                onNavigationButtonClick = { navController.popBackStackThrottled(lifecycleOwner) },
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        AnimatedContent(
            targetState = screenTimeScreenState.isInitializing,
            contentAlignment = Alignment.Center,
            label = "screenTimeScreenContentLoadingAnimation",
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) { isInitializing ->
            if (!isInitializing) {
                Column(modifier = Modifier.padding(paddingValues = paddingValues)) {
                    DailyScreenTimeChart(
                        screenTimeMinutesPerHourEntries =
                        screenTimeScreenState.screenTimeMinutesPerHourEntries,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(MaterialTheme.space.xxxLarge)
                            .padding(bottom = MaterialTheme.space.mediumLarge)
                    )

                    ElevatedCard(
                        colors = CardDefaults.cardColors(
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
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = MaterialTheme.space.medium)
                        ) {
                            Text(
                                text = stringResource(R.string.screen_time_colon),
                                style = MaterialTheme.typography.headlineMedium
                            )

                            screenTimeScreenState.todayScreenTimeDurationMillis?.let {
                                Text(
                                    text = getCompactDurationString(
                                        Duration(it, Duration.DisplayPrecision.MINUTES)
                                    ),
                                    style = MaterialTheme.typography.displayLarge
                                )
                            }
                        }
                    }

                    Text(
                        text = stringResource(R.string.screen_time_history),
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.medium
                        )
                    )

                    if (screenTimeScreenState.uiReadySessionEvents.isEmpty()) {
                        ElevatedCard(
                            colors = CardDefaults.cardColors(
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
                                    bottom = MaterialTheme.space.large,
                                )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = MaterialTheme.space.medium)
                            ) {
                                Text(
                                    text = stringResource(R.string.history_empty_for_that_day),
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = MaterialTheme.space.medium,
                                    end = MaterialTheme.space.medium,
                                    bottom = MaterialTheme.space.medium,
                                )
                        ) {
                            val timeFormat =
                                if (DateFormat.is24HourFormat(LocalContext.current))
                                    TimeFormat.MILITARY
                                else TimeFormat.AMPM

                            screenTimeScreenState.uiReadySessionEvents.forEach {
                                if (it is ScreenTimeScreenState.UIReadySessionEvent.ScreenTime) {
                                    SingleScreenTimeSessionCard(
                                        screenSessionStartAndEndTimesInMillis =
                                        it.startAndEndTimesInMillis,
                                        screenSessionDuration =
                                        Duration(it.screenSessionDurationMillis),
                                        timeFormat = timeFormat,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = MaterialTheme.space.medium)
                                    )
                                } else if (it is ScreenTimeScreenState.UIReadySessionEvent.CounterPaused) {
                                    CounterPauseSeparator(
                                        counterPauseSessionStartAndEndTimesInMillis =
                                        it.startAndEndTimesInMillis,
                                        timeFormat = timeFormat,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = MaterialTheme.space.medium)
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
}