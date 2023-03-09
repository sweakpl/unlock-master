package com.sweak.unlockmaster.presentation.main.screen_time

import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.github.mikephil.charting.data.Entry
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import com.sweak.unlockmaster.presentation.common.util.TimeFormat
import com.sweak.unlockmaster.presentation.main.screen_time.components.CounterPauseSeparator
import com.sweak.unlockmaster.presentation.main.screen_time.components.DailyScreenTimeChart
import com.sweak.unlockmaster.presentation.main.screen_time.components.SingleScreenTimeSessionCard

@Composable
fun ScreenTimeScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier.background(color = MaterialTheme.colors.background)
    ) {
        NavigationBar(
            title = stringResource(R.string.screen_time),
            onBackClick = { navController.popBackStack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            DailyScreenTimeChart(
                screenTimeMinutesPerHourEntries = listOf(
                    Entry(0f, 0f),
                    Entry(1f, 0f),
                    Entry(2f, 0f),
                    Entry(3f, 4f),
                    Entry(4f, 12f),
                    Entry(5f, 0f),
                    Entry(6f, 0f),
                    Entry(7f, 0f),
                    Entry(8f, 23f),
                    Entry(9f, 45f),
                    Entry(10f, 3f),
                    Entry(11f, 0f),
                    Entry(12f, 0f),
                    Entry(13f, 28f),
                    Entry(14f, 12f),
                    Entry(15f, 2f),
                    Entry(16f, 4f),
                    Entry(17f, 0f),
                    Entry(18f, 0f),
                    Entry(19f, 56f),
                    Entry(20f, 34f),
                    Entry(21f, 4f),
                    Entry(22f, 0f),
                    Entry(23f, 0f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.space.xxxLarge)
                    .padding(bottom = MaterialTheme.space.mediumLarge)
            )

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
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = MaterialTheme.space.medium)
                ) {
                    Text(
                        text = stringResource(R.string.todays_screen_time),
                        style = MaterialTheme.typography.h4
                    )

                    Text(
                        text = "1h 19m 10s",
                        style = MaterialTheme.typography.h1
                    )
                }
            }

            Text(
                text = stringResource(R.string.screen_time_history),
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(
                    start = MaterialTheme.space.medium,
                    end = MaterialTheme.space.medium,
                    bottom = MaterialTheme.space.medium
                )
            )

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
                    if (DateFormat.is24HourFormat(LocalContext.current)) TimeFormat.MILITARY
                    else TimeFormat.AMPM

                listOf(
                    SessionEvent.ScreenTimeSessionEvent(
                        Pair(1678345200000, 1678345260000),
                        Triple(0, 0, 15)
                    ),
                    SessionEvent.ScreenTimeSessionEvent(
                        Pair(1678350960000, 1678351260000),
                        Triple(0, 13, 2)
                    ),
                    SessionEvent.CounterPausedSessionEvent(
                        Pair(1678351260000, 1678354320000)
                    ),
                    SessionEvent.ScreenTimeSessionEvent(
                        Pair(1678354320000, 1678354620000),
                        Triple(0, 5, 0)
                    ),
                    SessionEvent.ScreenTimeSessionEvent(
                        Pair(1678356720000, 1678360380000),
                        Triple(1, 1, 12)
                    )
                ).forEach {
                    if (it is SessionEvent.ScreenTimeSessionEvent) {
                        SingleScreenTimeSessionCard(
                            screenSessionStartAndEndTimesInMillis =
                            it.screenSessionStartAndEndTimesInMillis,
                            screenSessionHoursMinutesAndSecondsDurationTriple =
                            it.screenSessionHoursMinutesAndSecondsDurationTriple,
                            timeFormat = timeFormat,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = MaterialTheme.space.medium)
                        )
                    } else if (it is SessionEvent.CounterPausedSessionEvent){
                        CounterPauseSeparator(
                            counterPauseSessionStartAndEndTimesInMillis =
                            it.counterPauseSessionStartAndEndTimesInMillis,
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
}

sealed class SessionEvent {
    data class ScreenTimeSessionEvent(
        val screenSessionStartAndEndTimesInMillis: Pair<Long, Long>,
        val screenSessionHoursMinutesAndSecondsDurationTriple: Triple<Int, Int, Int>
    ) : SessionEvent()
    data class CounterPausedSessionEvent(
        val counterPauseSessionStartAndEndTimesInMillis: Pair<Long, Long>
    ) : SessionEvent()
}