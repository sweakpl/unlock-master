package com.sweak.unlockmaster.presentation.settings.daily_wrap_up_settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.components.Dialog
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import com.sweak.unlockmaster.presentation.introduction.components.ProceedButton
import com.sweak.unlockmaster.presentation.settings.daily_wrap_up_settings.components.CardTimePicker

@Composable
fun DailyWrapUpSettingsScreen(
    dailyWrapUpSettingsViewModel: DailyWrapUpSettingsViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        dailyWrapUpSettingsViewModel.notificationTimeSubmittedEvents.collect {
            navController.popBackStack()
        }
    }

    val dailyWrapUpSettingsScreenState = dailyWrapUpSettingsViewModel.state

    Column(
        modifier = Modifier.background(color = MaterialTheme.colors.background)
    ) {
        NavigationBar(
            title = stringResource(R.string.daily_wrapups),
            onBackClick = { navController.popBackStack() }
        )

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(R.string.daily_wrapups),
                    style = MaterialTheme.typography.h1,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            top = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.small
                        )
                )

                Text(
                    text = stringResource(R.string.daily_wrapups_description),
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.medium
                        )
                )

                Image(
                    painter = painterResource(R.drawable.img_daily_wrapup_notification),
                    contentDescription = stringResource(
                        R.string.content_description_daily_wrapup_notification_image
                    ),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = MaterialTheme.space.mediumLarge,
                            end = MaterialTheme.space.mediumLarge,
                            bottom = MaterialTheme.space.mediumLarge,
                        )
                )

                Text(
                    text = stringResource(R.string.daily_wrapups_notifications_setting_description),
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.medium
                        )
                )

                if (dailyWrapUpSettingsScreenState.notificationHourOfDay != null &&
                    dailyWrapUpSettingsScreenState.notificationMinute != null
                ) {
                    CardTimePicker(
                        hourOfDay = dailyWrapUpSettingsScreenState.notificationHourOfDay,
                        minute = dailyWrapUpSettingsScreenState.notificationMinute,
                        onTimeChanged = { hourOfDay, minute ->
                            dailyWrapUpSettingsViewModel.onEvent(
                                DailyWrapUpSettingsScreenEvent.SelectNewDailyWrapUpSettingsNotificationsTime(
                                    newNotificationHourOfDay = hourOfDay,
                                    newNotificationMinute = minute
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = MaterialTheme.space.medium,
                                end = MaterialTheme.space.medium,
                                bottom = MaterialTheme.space.mediumLarge
                            )
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Card(
                    backgroundColor = Color.Transparent,
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colors.secondary
                    ),
                    elevation = MaterialTheme.space.default,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.space.medium)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(all = MaterialTheme.space.small)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(
                                R.string.content_description_info_icon
                            )
                        )

                        Text(
                            text = stringResource(
                                R.string.daily_wrap_up_notifications_can_only_be_delivered_between
                            ),
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier.padding(start = MaterialTheme.space.small)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.space.run { xLarge + 2 * medium }))
            }

            ProceedButton(
                text = stringResource(R.string.confirm),
                onClick = {
                    dailyWrapUpSettingsViewModel.onEvent(
                        DailyWrapUpSettingsScreenEvent.ConfirmNewSelectedDailyWrapUpSettings
                    )
                },
                modifier = Modifier.padding(all = MaterialTheme.space.medium)
            )
        }
    }

    if (dailyWrapUpSettingsScreenState.isInvalidTimeSelectedDialogVisible) {
        Dialog(
            title = stringResource(R.string.invalid_time_selected),
            message = stringResource(
                R.string.daily_wrap_up_notifications_can_only_be_delivered_between
            ),
            onDismissRequest = {
                dailyWrapUpSettingsViewModel.onEvent(
                    DailyWrapUpSettingsScreenEvent.IsInvalidTimeSelectedDialogVisible(
                        isVisible = false
                    )
                )
            },
            onPositiveClick = {
                dailyWrapUpSettingsViewModel.onEvent(
                    DailyWrapUpSettingsScreenEvent.IsInvalidTimeSelectedDialogVisible(
                        isVisible = false
                    )
                )
            },
            positiveButtonText = stringResource(R.string.ok)
        )
    }
}