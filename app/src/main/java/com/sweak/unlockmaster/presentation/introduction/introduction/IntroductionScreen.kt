package com.sweak.unlockmaster.presentation.introduction.introduction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Spa
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import com.sweak.unlockmaster.presentation.common.util.navigateThrottled
import com.sweak.unlockmaster.presentation.common.util.popBackStackThrottled
import com.sweak.unlockmaster.presentation.introduction.components.InformationCard
import com.sweak.unlockmaster.presentation.common.components.ProceedButton

@Composable
fun IntroductionScreen(
    navController: NavController,
    isLaunchedFromSettings: Boolean
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    Column(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
    ) {
        NavigationBar(
            title = stringResource(R.string.introduction),
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
                    text = stringResource(R.string.how_does_it_work),
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
                    text = stringResource(R.string.how_it_works_description),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.mediumLarge
                        )
                )

                InformationCard(
                    title = stringResource(R.string.guidance),
                    description = stringResource(R.string.guidance_description),
                    icon = Icons.Filled.NotificationsActive,
                    iconContentDescription = stringResource(
                        R.string.content_description_guidance_icon
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.medium
                        )
                )

                InformationCard(
                    title = stringResource(R.string.statistics),
                    description = stringResource(R.string.statistics_description),
                    icon = Icons.Filled.QueryStats,
                    iconContentDescription = stringResource(
                        R.string.content_description_statistics_icon
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.medium
                        )
                )

                InformationCard(
                    title = stringResource(R.string.wellness),
                    description = stringResource(R.string.wellness_description),
                    icon = Icons.Filled.Spa,
                    iconContentDescription = stringResource(
                        R.string.content_description_wellness_icon
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.space.medium)
                )

                Spacer(modifier = Modifier.height(MaterialTheme.space.run { xLarge + 2 * medium }))
            }

            ProceedButton(
                text = stringResource(R.string.got_it),
                onClick = {
                    if (isLaunchedFromSettings) {
                        navController.popBackStackThrottled(lifecycleOwner)
                    } else {
                        navController.navigateThrottled(
                            Screen.UnlockLimitSetupScreen.withArguments(false.toString()),
                            lifecycleOwner
                        )
                    }
                },
                modifier = Modifier.padding(all = MaterialTheme.space.medium)
            )
        }
    }
}