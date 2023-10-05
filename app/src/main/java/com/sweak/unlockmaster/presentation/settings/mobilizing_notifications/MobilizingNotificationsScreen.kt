package com.sweak.unlockmaster.presentation.settings.mobilizing_notifications

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import com.sweak.unlockmaster.presentation.common.util.popBackStackThrottled
import com.sweak.unlockmaster.presentation.introduction.components.ProceedButton
import com.sweak.unlockmaster.presentation.settings.mobilizing_notifications.components.ComboBox

@Composable
fun MobilizingNotificationsScreen(
    mobilizingNotificationsViewModel: MobilizingNotificationsViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = context) {
        mobilizingNotificationsViewModel.frequencyPercentageSubmittedEvents.collect {
            navController.popBackStackThrottled(lifecycleOwner)
        }
    }

    val mobilizingNotificationsScreenState = mobilizingNotificationsViewModel.state

    Column(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
    ) {
        NavigationBar(
            title = stringResource(R.string.mobilizing_notifications),
            onBackClick = { navController.popBackStackThrottled(lifecycleOwner) }
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

                Spacer(modifier = Modifier.height(MaterialTheme.space.run { xLarge + 2 * medium }))
            }

            ProceedButton(
                text = stringResource(R.string.confirm),
                onClick = {
                    mobilizingNotificationsViewModel.onEvent(
                        MobilizingNotificationsScreenEvent.ConfirmNewSelectedFrequencyPercentage
                    )
                },
                modifier = Modifier.padding(all = MaterialTheme.space.medium)
            )
        }
    }
}