package com.sweak.unlockmaster.presentation.daily_wrap_up

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.components.Dialog
import com.sweak.unlockmaster.presentation.common.theme.space
import com.sweak.unlockmaster.presentation.common.util.getFullDateString
import com.sweak.unlockmaster.presentation.common.util.navigateThrottled
import com.sweak.unlockmaster.presentation.common.util.popBackStackThrottled
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpCriterionPreviewCard
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpScreenOnEventsDetailsCard
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpScreenTimeDetailsCard
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpScreenTimeLimitDetailsCard
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpScreenUnlocksDetailsCard
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpUnlockLimitDetailsCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DailyWrapUpScreen(
    navController: NavController,
    dailyWrapUpDayTimeInMillis: Long,
    dailyWrapUpViewModel: DailyWrapUpViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val dailyWrapUpScreenState = dailyWrapUpViewModel.state
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

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
                        text = stringResource(R.string.daily_wrapup),
                        style = MaterialTheme.typography.displayMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStackThrottled(lifecycleOwner) }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = stringResource(
                                R.string.content_description_close_icon
                            )
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        AnimatedContent(
            targetState = dailyWrapUpScreenState.isInitializing,
            contentAlignment = Alignment.Center,
            label = "dailyWrapUpLoadingAnimation",
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) { isInitializing ->
            if (!isInitializing) {
                val scrollScope = rememberCoroutineScope()

                var screenUnlocksCardPosition by remember { mutableFloatStateOf(0f) }
                var screenTimeCardPosition by remember { mutableFloatStateOf(0f) }
                var unlockLimitCardPosition by remember { mutableFloatStateOf(0f) }
                var screenTimeLimitCardPosition by remember { mutableFloatStateOf(0f) }
                var screenOnEventsCardPosition by remember { mutableFloatStateOf(0f) }

                val navBarWithPaddingHeight = with(LocalDensity.current) {
                    MaterialTheme.space.run { xxLarge + medium }.toPx()
                }

                Column(modifier = Modifier.padding(paddingValues = paddingValues)) {
                    Text(
                        text = stringResource(R.string.heres_how_you_did_today),
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.padding(
                            start = MaterialTheme.space.medium,
                            top = MaterialTheme.space.mediumLarge,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.small
                        )
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = MaterialTheme.space.medium)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarMonth,
                            contentDescription = stringResource(
                                R.string.content_description_calendar_icon
                            )
                        )

                        Text(
                            text = getFullDateString(dailyWrapUpDayTimeInMillis),
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(start = MaterialTheme.space.medium)
                        )
                    }

                    FlowRow(
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.space.medium),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.space.medium),
                        maxItemsInEachRow = 2,
                        modifier = Modifier.padding(
                            start = MaterialTheme.space.medium,
                            top = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.large
                        )
                    ) {
                        if (dailyWrapUpScreenState.screenUnlocksPreviewData != null) {
                            DailyWrapUpCriterionPreviewCard(
                                dailyWrapUpCriterionPreviewType =
                                dailyWrapUpScreenState.screenUnlocksPreviewData,
                                onClick = {
                                    scrollScope.launch {
                                        scrollState.animateScrollBy(
                                            screenUnlocksCardPosition - navBarWithPaddingHeight
                                        )
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (dailyWrapUpScreenState.screenTimePreviewData != null) {
                            DailyWrapUpCriterionPreviewCard(
                                dailyWrapUpCriterionPreviewType =
                                dailyWrapUpScreenState.screenTimePreviewData,
                                onClick = {
                                    scrollScope.launch {
                                        scrollState.animateScrollBy(
                                            screenTimeCardPosition - navBarWithPaddingHeight
                                        )
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (dailyWrapUpScreenState.unlockLimitPreviewData != null) {
                            DailyWrapUpCriterionPreviewCard(
                                dailyWrapUpCriterionPreviewType =
                                dailyWrapUpScreenState.unlockLimitPreviewData,
                                onClick = {
                                    scrollScope.launch {
                                        scrollState.animateScrollBy(
                                            unlockLimitCardPosition - navBarWithPaddingHeight
                                        )
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (dailyWrapUpScreenState.screenTimeLimitPreviewData != null) {
                            DailyWrapUpCriterionPreviewCard(
                                dailyWrapUpCriterionPreviewType =
                                dailyWrapUpScreenState.screenTimeLimitPreviewData,
                                onClick = {
                                    scrollScope.launch {
                                        scrollState.animateScrollBy(
                                            screenTimeLimitCardPosition - navBarWithPaddingHeight
                                        )
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (dailyWrapUpScreenState.screenOnEventsPreviewData != null) {
                            DailyWrapUpCriterionPreviewCard(
                                dailyWrapUpCriterionPreviewType =
                                dailyWrapUpScreenState.screenOnEventsPreviewData,
                                onClick = {
                                    scrollScope.launch {
                                        scrollState.animateScrollBy(
                                            screenOnEventsCardPosition - navBarWithPaddingHeight
                                        )
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (dailyWrapUpScreenState.screenTimeLimitPreviewData != null) {
                            // Add a Spacer to prevent the only element in the FlowRow row
                            // expand to full width:
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }

                    if (dailyWrapUpScreenState.screenUnlocksDetailsData != null) {
                        DailyWrapUpScreenUnlocksDetailsCard(
                            detailsData = dailyWrapUpScreenState.screenUnlocksDetailsData,
                            onInteraction = {
                                navController.navigateThrottled(
                                    Screen.StatisticsScreen.route,
                                    lifecycleOwner
                                )
                            },
                            modifier = Modifier
                                .padding(
                                    start = MaterialTheme.space.medium,
                                    end = MaterialTheme.space.medium,
                                    bottom = MaterialTheme.space.large
                                )
                                .fillMaxWidth()
                                .onGloballyPositioned {
                                    screenUnlocksCardPosition = it.positionInRoot().y
                                }
                        )
                    }

                    if (dailyWrapUpScreenState.screenTimeDetailsData != null) {
                        DailyWrapUpScreenTimeDetailsCard(
                            detailsData = dailyWrapUpScreenState.screenTimeDetailsData,
                            onInteraction = {
                                navController.navigateThrottled(
                                    Screen.ScreenTimeScreen.withArguments(
                                        dailyWrapUpDayTimeInMillis.toString()
                                    ),
                                    lifecycleOwner
                                )
                            },
                            modifier = Modifier
                                .padding(
                                    start = MaterialTheme.space.medium,
                                    end = MaterialTheme.space.medium,
                                    bottom = MaterialTheme.space.large
                                )
                                .fillMaxWidth()
                                .onGloballyPositioned {
                                    screenTimeCardPosition = it.positionInRoot().y
                                }
                        )
                    }

                    if (dailyWrapUpScreenState.unlockLimitDetailsData != null) {
                        DailyWrapUpUnlockLimitDetailsCard(
                            detailsData = dailyWrapUpScreenState.unlockLimitDetailsData,
                            onInteraction = {
                                dailyWrapUpViewModel.onEvent(
                                    DailyWrapUpScreenEvent.ApplySuggestedUnlockLimit
                                )
                            },
                            modifier = Modifier
                                .padding(
                                    start = MaterialTheme.space.medium,
                                    end = MaterialTheme.space.medium,
                                    bottom = MaterialTheme.space.large
                                )
                                .fillMaxWidth()
                                .onGloballyPositioned {
                                    unlockLimitCardPosition = it.positionInRoot().y
                                }
                        )
                    }

                    if (dailyWrapUpScreenState.screenTimeLimitDetailsData != null) {
                        DailyWrapUpScreenTimeLimitDetailsCard(
                            detailsData = dailyWrapUpScreenState.screenTimeLimitDetailsData,
                            onInteraction = {
                                dailyWrapUpViewModel.onEvent(
                                    DailyWrapUpScreenEvent.ApplySuggestedScreenTimeLimit
                                )
                            },
                            modifier = Modifier
                                .padding(
                                    start = MaterialTheme.space.medium,
                                    end = MaterialTheme.space.medium,
                                    bottom = MaterialTheme.space.large
                                )
                                .fillMaxWidth()
                                .onGloballyPositioned {
                                    screenTimeLimitCardPosition = it.positionInRoot().y
                                }
                        )
                    }

                    if (dailyWrapUpScreenState.screenOnEventsDetailsData != null) {
                        DailyWrapUpScreenOnEventsDetailsCard(
                            detailsData = dailyWrapUpScreenState.screenOnEventsDetailsData,
                            onInteraction = {
                                dailyWrapUpViewModel.onEvent(
                                    DailyWrapUpScreenEvent.ScreenOnEventsInformationDialogVisible(
                                        isVisible = true
                                    )
                                )
                            },
                            modifier = Modifier
                                .padding(
                                    start = MaterialTheme.space.medium,
                                    end = MaterialTheme.space.medium,
                                    bottom = MaterialTheme.space.large
                                )
                                .fillMaxWidth()
                                .onGloballyPositioned {
                                    screenOnEventsCardPosition = it.positionInRoot().y
                                }
                        )
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

    if (dailyWrapUpScreenState.isScreenOnEventsInformationDialogVisible) {
        Dialog(
            title = stringResource(R.string.screen_on_events),
            message = stringResource(R.string.screen_on_event_description),
            onDismissRequest = {
                dailyWrapUpViewModel.onEvent(
                    DailyWrapUpScreenEvent.ScreenOnEventsInformationDialogVisible(
                        isVisible = false
                    )
                )
            },
            onPositiveClick = {
                dailyWrapUpViewModel.onEvent(
                    DailyWrapUpScreenEvent.ScreenOnEventsInformationDialogVisible(
                        isVisible = false
                    )
                )
            },
            positiveButtonText = stringResource(R.string.ok)
        )
    }
}