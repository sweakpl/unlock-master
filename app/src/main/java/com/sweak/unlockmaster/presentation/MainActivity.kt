package com.sweak.unlockmaster.presentation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import com.sweak.unlockmaster.domain.use_case.daily_wrap_ups.ScheduleDailyWrapUpsNotificationsUseCase
import com.sweak.unlockmaster.domain.use_case.screen_on_events.AddScreenOnEventUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_events.AddUnlockEventUseCase
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.Screen.Companion.KEY_DISPLAYED_SCREEN_TIME_DAY_MILLIS
import com.sweak.unlockmaster.presentation.common.Screen.Companion.KEY_IS_LAUNCHED_FROM_SETTINGS
import com.sweak.unlockmaster.presentation.common.Screen.Companion.KEY_IS_UPDATING_EXISTING_UNLOCK_LIMIT
import com.sweak.unlockmaster.presentation.common.ui.theme.UnlockMasterTheme
import com.sweak.unlockmaster.presentation.introduction.background_work.WorkInBackgroundScreen
import com.sweak.unlockmaster.presentation.introduction.introduction.IntroductionScreen
import com.sweak.unlockmaster.presentation.introduction.limit_setup.UnlockLimitSetupScreen
import com.sweak.unlockmaster.presentation.introduction.setup_complete.SetupCompleteScreen
import com.sweak.unlockmaster.presentation.introduction.welcome.WelcomeScreen
import com.sweak.unlockmaster.presentation.main.home.HomeScreen
import com.sweak.unlockmaster.presentation.main.screen_time.ScreenTimeScreen
import com.sweak.unlockmaster.presentation.main.statistics.StatisticsScreen
import com.sweak.unlockmaster.presentation.settings.SettingsScreen
import com.sweak.unlockmaster.presentation.settings.daily_wrap_ups_setting.DailyWrapUpsSettingScreen
import com.sweak.unlockmaster.presentation.settings.mobilizing_notifications.MobilizingNotificationsScreen
import com.sweak.unlockmaster.presentation.background_work.UnlockMasterService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userSessionRepository: UserSessionRepository

    @Inject
    lateinit var addUnlockEventUseCase: AddUnlockEventUseCase

    @Inject
    lateinit var addScreenOnEventUseCase: AddScreenOnEventUseCase

    @Inject
    lateinit var scheduleDailyWrapUpsNotificationsUseCase: ScheduleDailyWrapUpsNotificationsUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UnlockMasterTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination =
                    if (runBlocking { userSessionRepository.isIntroductionFinished() })
                        Screen.HomeScreen.route
                    else Screen.WelcomeScreen.route
                ) {
                    composable(route = Screen.WelcomeScreen.route) {
                        WelcomeScreen(navController = navController)
                    }

                    composable(
                        route = Screen.IntroductionScreen.route
                                + "/{$KEY_IS_LAUNCHED_FROM_SETTINGS}",
                        arguments = listOf(
                            navArgument(KEY_IS_LAUNCHED_FROM_SETTINGS) {
                                type = NavType.BoolType
                                nullable = false
                            }
                        )
                    ) {
                        IntroductionScreen(
                            navController = navController,
                            isLaunchedFromSettings =
                            it.arguments?.getBoolean(KEY_IS_LAUNCHED_FROM_SETTINGS) ?: true
                        )
                    }

                    composable(
                        route = Screen.UnlockLimitSetupScreen.route
                                + "/{$KEY_IS_UPDATING_EXISTING_UNLOCK_LIMIT}",
                        arguments = listOf(
                            navArgument(KEY_IS_UPDATING_EXISTING_UNLOCK_LIMIT) {
                                type = NavType.BoolType
                                nullable = false
                            }
                        )
                    ) {
                        UnlockLimitSetupScreen(
                            navController = navController,
                            isUpdatingExistingUnlockLimit =
                            it.arguments?.getBoolean(KEY_IS_UPDATING_EXISTING_UNLOCK_LIMIT) ?: true
                        )
                    }

                    composable(
                        route = Screen.WorkInBackgroundScreen.route
                                + "/{$KEY_IS_LAUNCHED_FROM_SETTINGS}",
                        arguments = listOf(
                            navArgument(KEY_IS_LAUNCHED_FROM_SETTINGS) {
                                type = NavType.BoolType
                                nullable = false
                            }
                        )
                    ) {
                        WorkInBackgroundScreen(
                            navController = navController,
                            onWorkInBackgroundAllowed = {
                                lifecycleScope.launch {
                                    // We're adding an unlock event for the data to be coherent as
                                    // the next lock event that will be caught has to have
                                    // a corresponding unlock event:
                                    addUnlockEventUseCase()
                                    addScreenOnEventUseCase()

                                    userSessionRepository.setIntroductionFinished()
                                    startUnlockMasterService()
                                    scheduleDailyWrapUpsNotificationsUseCase()
                                }
                            },
                            isLaunchedFromSettings =
                            it.arguments?.getBoolean(KEY_IS_LAUNCHED_FROM_SETTINGS) ?: true
                        )
                    }

                    composable(route = Screen.SetupCompleteScreen.route) {
                        SetupCompleteScreen(navController = navController)
                    }

                    composable(route = Screen.HomeScreen.route) {
                        HomeScreen(navController = navController)
                    }

                    composable(route = Screen.SettingsScreen.route) {
                        SettingsScreen(navController = navController)
                    }

                    composable(
                        route = Screen.ScreenTimeScreen.route
                                + "/{$KEY_DISPLAYED_SCREEN_TIME_DAY_MILLIS}",
                        arguments = listOf(
                            navArgument(KEY_DISPLAYED_SCREEN_TIME_DAY_MILLIS) {
                                type = NavType.LongType
                                nullable = false
                            }
                        )
                    ) {
                        ScreenTimeScreen(
                            navController = navController,
                            displayedScreenTimeDayTimeInMillis =
                            it.arguments?.getLong(KEY_DISPLAYED_SCREEN_TIME_DAY_MILLIS)
                                ?: System.currentTimeMillis()
                        )
                    }

                    composable(route = Screen.StatisticsScreen.route) {
                        StatisticsScreen(navController = navController)
                    }

                    composable(route = Screen.MobilizingNotificationsScreen.route) {
                        MobilizingNotificationsScreen(navController = navController)
                    }

                    composable(route = Screen.DailyWrapUpsSettingScreen.route) {
                        DailyWrapUpsSettingScreen(navController = navController)
                    }
                }
            }
        }
    }

    private fun startUnlockMasterService() {
        val serviceIntent = Intent(this, UnlockMasterService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }
}