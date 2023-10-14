package com.sweak.unlockmaster.presentation.introduction.introduction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.components.ProceedButton
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import com.sweak.unlockmaster.presentation.common.util.navigateThrottled
import com.sweak.unlockmaster.presentation.common.util.popBackStackThrottled
import com.sweak.unlockmaster.presentation.common.components.InformationCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntroductionScreen(
    navController: NavController,
    isLaunchedFromSettings: Boolean
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(
            connection = scrollBehavior.nestedScrollConnection
        ),
        topBar = {
            NavigationBar(
                title = stringResource(R.string.introduction),
                onNavigationButtonClick = { navController.popBackStackThrottled(lifecycleOwner) },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
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
                modifier = Modifier.padding(horizontal = MaterialTheme.space.medium)
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(paddingValues = it)) {
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
        }
    }
}