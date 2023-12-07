package com.sweak.unlockmaster.presentation.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.theme.space
import com.sweak.unlockmaster.presentation.common.util.navigateThrottled
import com.sweak.unlockmaster.presentation.common.util.popBackStackThrottled
import com.sweak.unlockmaster.presentation.settings.components.SettingsEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(
            connection = scrollBehavior.nestedScrollConnection
        ),
        topBar = {
            NavigationBar(
                title = stringResource(R.string.settings),
                onNavigationButtonClick = { navController.popBackStackThrottled(lifecycleOwner) },
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(paddingValues = it)) {
                SettingsEntry(
                    settingsEntryTitle = stringResource(R.string.mobilizing_notifications),
                    onEntryClick = {
                        navController.navigateThrottled(
                            Screen.MobilizingNotificationsScreen.route,
                            lifecycleOwner
                        )
                    },
                    modifier = Modifier.padding(horizontal = MaterialTheme.space.medium)
                )

                SettingsEntry(
                    settingsEntryTitle = stringResource(R.string.daily_wrapups),
                    onEntryClick = {
                        navController.navigateThrottled(
                            Screen.DailyWrapUpSettingsScreen.route,
                            lifecycleOwner
                        )
                    },
                    modifier = Modifier.padding(horizontal = MaterialTheme.space.medium)
                )

                SettingsEntry(
                    settingsEntryTitle = stringResource(R.string.work_in_background),
                    onEntryClick = {
                        navController.navigateThrottled(
                            Screen.WorkInBackgroundScreen.withArguments(true.toString()),
                            lifecycleOwner
                        )
                    },
                    modifier = Modifier.padding(horizontal = MaterialTheme.space.medium)
                )

                SettingsEntry(
                    settingsEntryTitle = stringResource(R.string.introduction),
                    onEntryClick = {
                        navController.navigateThrottled(
                            Screen.IntroductionScreen.withArguments(true.toString()),
                            lifecycleOwner
                        )
                    },
                    modifier = Modifier.padding(horizontal = MaterialTheme.space.medium)
                )

                SettingsEntry(
                    settingsEntryTitle = stringResource(R.string.user_interface_theme),
                    onEntryClick = {
                        navController.navigateThrottled(
                            Screen.UserInterfaceThemeScreen.route,
                            lifecycleOwner
                        )
                    },
                    modifier = Modifier.padding(horizontal = MaterialTheme.space.medium)
                )
            }
        }
    }
}