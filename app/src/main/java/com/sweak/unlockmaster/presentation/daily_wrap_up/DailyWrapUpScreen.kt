package com.sweak.unlockmaster.presentation.daily_wrap_up

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.components.Dialog
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import com.sweak.unlockmaster.presentation.common.util.getFullDateString
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpCriterionPreviewCard
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpScreenOnEventsDetailsCard
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpScreenTimeDetailsCard
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpScreenUnlocksDetailsCard
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpUnlockLimitDetailsCard
import kotlinx.coroutines.launch

@Composable
fun DailyWrapUpScreen(
    navController: NavController,
    dailyWrapUpDayTimeInMillis: Long,
    dailyWrapUpViewModel: DailyWrapUpViewModel = hiltViewModel()
) {
    val dailyWrapUpScreenState = dailyWrapUpViewModel.state

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
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = stringResource(R.string.content_description_close_icon)
                )
            }

            Text(
                text = stringResource(R.string.daily_wrapup),
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
            targetState = dailyWrapUpScreenState.isInitializing,
            contentAlignment = Alignment.Center,
            label = "dailyWrapUpLoadingAnimation",
            modifier = Modifier.fillMaxWidth()
        ) { isInitializing ->
            if (!isInitializing) {
                val scrollState = rememberScrollState()
                val scrollScope = rememberCoroutineScope()

                var screenUnlocksCardPosition by remember { mutableFloatStateOf(0f) }
                var screenTimeCardPosition by remember { mutableFloatStateOf(0f) }
                var unlockLimitCardPosition by remember { mutableFloatStateOf(0f) }
                var screenOnEventsCardPosition by remember { mutableFloatStateOf(0f) }

                val navBarWithPaddingHeight = with(LocalDensity.current) {
                    MaterialTheme.space.run { xxLarge + medium }.toPx()
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    Text(
                        text = stringResource(R.string.heres_how_you_did_today),
                        style = MaterialTheme.typography.h1,
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
                            style = MaterialTheme.typography.h4,
                            modifier = Modifier.padding(start = MaterialTheme.space.medium)
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.space.medium),
                        modifier = Modifier.padding(
                            start = MaterialTheme.space.medium,
                            top = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.large
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.space.medium)
                        ) {
                            DailyWrapUpCriterionPreviewCard(
                                dailyWrapUpCriterionPreviewType =
                                dailyWrapUpScreenState.screenUnlocksPreviewData!!,
                                onClick = {
                                    scrollScope.launch {
                                        scrollState.animateScrollBy(
                                            screenUnlocksCardPosition - navBarWithPaddingHeight
                                        )
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )

                            DailyWrapUpCriterionPreviewCard(
                                dailyWrapUpCriterionPreviewType =
                                dailyWrapUpScreenState.screenTimePreviewData!!,
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

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.space.medium)
                        ) {
                            DailyWrapUpCriterionPreviewCard(
                                dailyWrapUpCriterionPreviewType =
                                dailyWrapUpScreenState.unlockLimitPreviewData!!,
                                onClick = {
                                    scrollScope.launch {
                                        scrollState.animateScrollBy(
                                            unlockLimitCardPosition - navBarWithPaddingHeight
                                        )
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )

                            DailyWrapUpCriterionPreviewCard(
                                dailyWrapUpCriterionPreviewType =
                                dailyWrapUpScreenState.screenOnEventsPreviewData!!,
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
                    }

                    DailyWrapUpScreenUnlocksDetailsCard(
                        detailsData = dailyWrapUpScreenState.screenUnlocksDetailsData!!,
                        onInteraction = {
                            navController.navigate(Screen.StatisticsScreen.route)
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

                    DailyWrapUpScreenTimeDetailsCard(
                        detailsData = dailyWrapUpScreenState.screenTimeDetailsData!!,
                        onInteraction = {
                            navController.navigate(
                                Screen.ScreenTimeScreen.withArguments(
                                    dailyWrapUpDayTimeInMillis.toString()
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
                                screenTimeCardPosition = it.positionInRoot().y
                            }
                    )

                    DailyWrapUpUnlockLimitDetailsCard(
                        detailsData = dailyWrapUpScreenState.unlockLimitDetailsData!!,
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

                    DailyWrapUpScreenOnEventsDetailsCard(
                        detailsData = dailyWrapUpScreenState.screenOnEventsDetailsData!!,
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

                    if (dailyWrapUpScreenState.haveAllDailyWrapUpFeaturesBeenDiscovered) {
                        Card(
                            elevation = MaterialTheme.space.xSmall,
                            modifier = Modifier
                                .padding(
                                    start = MaterialTheme.space.medium,
                                    end = MaterialTheme.space.medium,
                                    bottom = MaterialTheme.space.large
                                )
                                .fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(all = MaterialTheme.space.smallMedium)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.TipsAndUpdates,
                                    contentDescription = stringResource(
                                        R.string.content_description_tips_icon
                                    ),
                                    modifier = Modifier.size(size = MaterialTheme.space.mediumLarge)
                                )

                                Text(
                                    text = stringResource(
                                        R.string.you_ve_not_discovered_full_daily_wrap_up
                                    ),
                                    style = MaterialTheme.typography.subtitle1,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = MaterialTheme.space.smallMedium)
                                )
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