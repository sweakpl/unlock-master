package com.sweak.unlockmaster.presentation.settings.mobilizing_notifications

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.components.ObserveAsEvents
import com.sweak.unlockmaster.presentation.common.components.ProceedButton
import com.sweak.unlockmaster.presentation.common.theme.space
import com.sweak.unlockmaster.presentation.common.util.popBackStackThrottled
import com.sweak.unlockmaster.presentation.settings.mobilizing_notifications.components.ComboBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MobilizingNotificationsScreen(
    mobilizingNotificationsViewModel: MobilizingNotificationsViewModel = hiltViewModel(),
    navController: NavController
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    ObserveAsEvents(
        flow = mobilizingNotificationsViewModel.frequencyPercentageSubmittedEvents,
        onEvent = {
            navController.popBackStackThrottled(lifecycleOwner)
        }
    )

    val mobilizingNotificationsScreenState = mobilizingNotificationsViewModel.state
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(
            connection = scrollBehavior.nestedScrollConnection
        ),
        topBar = {
            NavigationBar(
                title = stringResource(R.string.mobilizing_notifications),
                onNavigationButtonClick = { navController.popBackStackThrottled(lifecycleOwner) },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ProceedButton(
                text = stringResource(R.string.confirm),
                onClick = {
                    mobilizingNotificationsViewModel.onEvent(
                        MobilizingNotificationsScreenEvent.ConfirmSelectedSettings
                    )
                },
                modifier = Modifier.padding(horizontal = MaterialTheme.space.medium)
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(paddingValues = paddingValues)) {
                Text(
                    text = stringResource(R.string.mobilizing_notifications),
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            top = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.small
                        )
                )

                Text(
                    text = stringResource(R.string.mobilizing_notifications_description),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.medium
                        )
                )

                Image(
                    painter = painterResource(R.drawable.img_mobilizing_notification),
                    contentDescription = stringResource(
                        R.string.content_description_mobilizing_notification_image
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
                    text = stringResource(R.string.mobilizing_notifications_setting_description),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.medium
                        )
                )

                if (mobilizingNotificationsScreenState.availableMobilizingNotificationsFrequencyPercentages != null &&
                    mobilizingNotificationsScreenState.selectedMobilizingNotificationsFrequencyPercentageIndex != null
                ) {
                    ComboBox(
                        menuItems = mobilizingNotificationsScreenState.availableMobilizingNotificationsFrequencyPercentages.map {
                            stringResource(R.string.every_x_percent, it)
                        },
                        selectedIndex = mobilizingNotificationsScreenState.selectedMobilizingNotificationsFrequencyPercentageIndex,
                        onMenuItemClick = {
                            mobilizingNotificationsViewModel.onEvent(
                                MobilizingNotificationsScreenEvent.SelectNewFrequencyPercentageIndex(
                                    newPercentageIndex = it
                                )
                            )
                        },
                        modifier = Modifier
                            .padding(
                                start = MaterialTheme.space.medium,
                                end = MaterialTheme.space.medium,
                                bottom = MaterialTheme.space.mediumLarge
                            )
                    )
                }

                Text(
                    text = stringResource(R.string.example),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.xSmall
                        )
                )

                Text(
                    text = stringResource(R.string.mobilizing_notifications_setting_example),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = MaterialTheme.space.medium)
                )

                if (mobilizingNotificationsScreenState.areOverLimitNotificationsEnabled != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = MaterialTheme.space.medium,
                                top = MaterialTheme.space.large,
                                end = MaterialTheme.space.medium
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = MaterialTheme.space.medium)
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = MaterialTheme.space.medium)
                            ) {
                                Text(
                                    text = stringResource(R.string.notify_over_100),
                                    style = MaterialTheme.typography.displaySmall
                                )

                                Text(
                                    text = stringResource(R.string.notify_over_100_description),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }

                            Switch(
                                checked =
                                mobilizingNotificationsScreenState.areOverLimitNotificationsEnabled,
                                onCheckedChange = {
                                    mobilizingNotificationsViewModel.onEvent(
                                        MobilizingNotificationsScreenEvent
                                            .ToggleOverLimitNotifications(
                                                areOverLimitNotificationsEnabled = it
                                            )
                                    )
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.secondary,
                                    checkedTrackColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.space.run { xLarge + 2 * medium }))
            }
        }
    }
}