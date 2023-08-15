package com.sweak.unlockmaster.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import com.sweak.unlockmaster.presentation.settings.components.SettingsEntry

@Composable
fun SettingsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        NavigationBar(
            title = stringResource(R.string.settings),
            onBackClick = { navController.popBackStack() }
        )

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            SettingsEntry(
                settingsEntryTitle = stringResource(R.string.mobilizing_notifications),
                onEntryClick = {
                    navController.navigate(Screen.MobilizingNotificationsScreen.route)
                },
                modifier = Modifier.padding(horizontal = MaterialTheme.space.medium)
            )

            SettingsEntry(
                settingsEntryTitle = stringResource(R.string.daily_wrapups),
                onEntryClick = {
                    navController.navigate(Screen.DailyWrapUpSettingsScreen.route)
                },
                modifier = Modifier.padding(horizontal = MaterialTheme.space.medium)
            )

            SettingsEntry(
                settingsEntryTitle = stringResource(R.string.work_in_background),
                onEntryClick = {
                    navController.navigate(
                        Screen.WorkInBackgroundScreen.withArguments(true.toString())
                    )
                },
                modifier = Modifier.padding(horizontal = MaterialTheme.space.medium)
            )

            SettingsEntry(
                settingsEntryTitle = stringResource(R.string.introduction),
                onEntryClick = {
                    navController.navigate(Screen.IntroductionScreen.withArguments(true.toString()))
                },
                modifier = Modifier.padding(horizontal = MaterialTheme.space.medium)
            )
        }
    }
}