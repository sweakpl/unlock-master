package com.sweak.unlockmaster.presentation.main.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.mikephil.charting.data.BarEntry
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.components.Dialog
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import com.sweak.unlockmaster.presentation.main.statistics.components.AllTimeUnlocksChart

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StatisticsScreen(
    navController: NavController
) {
    var isScreenOnEventsInformationDialogVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        NavigationBar(
            title = stringResource(R.string.statistics),
            onBackClick = { navController.popBackStack() }
        )

        AllTimeUnlocksChart(
            allTimeUnlockEventCountsEntries = listOf(
                BarEntry(0f, 37f),
                BarEntry(1f, 38f),
                BarEntry(2f, 36f),
                BarEntry(3f, 35f),
                BarEntry(4f, 35f),
                BarEntry(5f, 37f),
                BarEntry(6f, 34f),
                BarEntry(7f, 35f),
                BarEntry(8f, 36f),
                BarEntry(9f, 35f),
                BarEntry(10f, 33f),
                BarEntry(11f, 30f),
                BarEntry(12f, 31f),
                BarEntry(13f, 30f)
            ),
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
                text = "25 February 2023, Saturday",
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
                    modifier = Modifier.padding(bottom = MaterialTheme.space.mediumLarge)
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
                        text = "21",
                        style = MaterialTheme.typography.h1
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = MaterialTheme.space.mediumLarge)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MyLocation,
                        contentDescription = stringResource(
                            R.string.content_description_crosshair_icon
                        ),
                        modifier = Modifier.size(size = MaterialTheme.space.mediumLarge)
                    )

                    Text(
                        text = stringResource(R.string.unlock_limit),
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier
                            .padding(horizontal = MaterialTheme.space.smallMedium)
                            .weight(1f)
                    )

                    Text(
                        text = "30",
                        style = MaterialTheme.typography.h1
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = MaterialTheme.space.mediumLarge)
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
                        modifier = Modifier.padding(start = MaterialTheme.space.smallMedium)
                    )

                    CompositionLocalProvider(
                        LocalMinimumInteractiveComponentEnforcement provides false
                    ) {
                        IconButton(
                            onClick = { isScreenOnEventsInformationDialogVisible = true },
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
                                modifier = Modifier.size(size = MaterialTheme.space.smallMedium)
                            )
                        }
                    }

                    Text(
                        text = "49",
                        style = MaterialTheme.typography.h1,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = MaterialTheme.space.mediumLarge)
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

                    Text(
                        text = "1h 15min",
                        style = MaterialTheme.typography.h1
                    )
                }

                Button(
                    onClick = {
                        // TODO navigate to screen time details
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

    if (isScreenOnEventsInformationDialogVisible) {
        Dialog(
            title = stringResource(R.string.screen_on_events),
            message = stringResource(R.string.screen_on_event_description),
            onDismissRequest = {
                isScreenOnEventsInformationDialogVisible = false
            },
            onPositiveClick = {
                isScreenOnEventsInformationDialogVisible = false
            },
            positiveButtonText = stringResource(R.string.ok)
        )
    }
}