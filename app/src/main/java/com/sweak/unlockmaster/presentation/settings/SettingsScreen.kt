package com.sweak.unlockmaster.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import com.sweak.unlockmaster.presentation.common.util.navigateThrottled
import com.sweak.unlockmaster.presentation.common.util.popBackStackThrottled
import com.sweak.unlockmaster.presentation.settings.components.SettingsEntry

@Composable
fun SettingsScreen(navController: NavController) {
    val lifecycleOwner = LocalLifecycleOwner.current

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        NavigationBar(
            title = stringResource(R.string.settings),
            onBackClick = { navController.popBackStackThrottled(lifecycleOwner) }
        )

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
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
        }
    }
}