package com.sweak.unlockmaster.presentation.settings.daily_wrap_ups

import android.annotation.SuppressLint
import android.os.Build
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.widget.TimePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.times
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import com.sweak.unlockmaster.presentation.introduction.components.ProceedButton

@SuppressLint("InflateParams")
@Composable
fun DailyWrapUpsScreen(
    dailyWrapUpsViewModel: DailyWrapUpsViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        dailyWrapUpsViewModel.notificationTimeSubmittedEvents.collect {
            navController.popBackStack()
        }
    }

    val dailyWrapUpsScreenState = dailyWrapUpsViewModel.state

    val is24HourFormat = DateFormat.is24HourFormat(context)

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

                if (dailyWrapUpsScreenState.notificationHourOfDay != null &&
                    dailyWrapUpsScreenState.notificationMinute != null
                ) {
                    Card(
                        elevation = MaterialTheme.space.xSmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = MaterialTheme.space.medium,
                                end = MaterialTheme.space.medium,
                                bottom = MaterialTheme.space.mediumLarge
                            )
                    ) {
                        AndroidView(
                            factory = {
                                (LayoutInflater.from(it)
                                    .inflate(R.layout.spinner_time_picker, null) as TimePicker)
                                    .apply {
                                        // Right after composing the TimePicker it calls the
                                        // timeChangedListener with the current time which breaks the
                                        // uiState - we have to prevent the uiState update after this
                                        // initial timeChangedListener call:
                                        var isInitialUpdate = true

                                        setOnTimeChangedListener { _, hourOfDay, minute ->
                                            if (!isInitialUpdate) {
                                                dailyWrapUpsViewModel.onEvent(
                                                    DailyWrapUpsScreenEvent.SelectNewDailyWrapUpsNotificationsTime(
                                                        newNotificationHourOfDay = hourOfDay,
                                                        newNotificationMinute = minute
                                                    )
                                                )
                                            } else {
                                                isInitialUpdate = false
                                            }
                                        }
                                    }
                            },
                            update = {
                                it.setIs24HourView(is24HourFormat)

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    it.hour = dailyWrapUpsScreenState.notificationHourOfDay
                                    it.minute = dailyWrapUpsScreenState.notificationMinute
                                } else {
                                    it.currentHour = dailyWrapUpsScreenState.notificationHourOfDay
                                    it.currentMinute = dailyWrapUpsScreenState.notificationMinute
                                }
                            },
                            modifier = Modifier.padding(all = MaterialTheme.space.medium)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.space.run { xLarge + 2 * medium }))
            }

            ProceedButton(
                text = stringResource(R.string.confirm),
                onClick = {
                    dailyWrapUpsViewModel.onEvent(
                        DailyWrapUpsScreenEvent.ConfirmNewSelectedDailyWrapUpsNotificationsTime
                    )
                },
                modifier = Modifier.padding(all = MaterialTheme.space.medium)
            )
        }
    }
}