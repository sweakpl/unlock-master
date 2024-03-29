package com.sweak.unlockmaster.presentation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sweak.unlockmaster.domain.model.UiThemeMode
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import com.sweak.unlockmaster.domain.use_case.daily_wrap_up.ScheduleDailyWrapUpNotificationsUseCase
import com.sweak.unlockmaster.domain.use_case.screen_on_events.AddScreenOnEventUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_events.AddUnlockEventUseCase
import com.sweak.unlockmaster.presentation.background_work.EXTRA_DAILY_WRAP_UP_DAY_MILLIS
import com.sweak.unlockmaster.presentation.background_work.EXTRA_SHOW_DAILY_WRAP_UP_SCREEN
import com.sweak.unlockmaster.presentation.background_work.UnlockMasterService
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.Screen.Companion.KEY_DAILY_WRAP_UP_DAY_MILLIS
import com.sweak.unlockmaster.presentation.common.Screen.Companion.KEY_DISPLAYED_SCREEN_TIME_DAY_MILLIS
import com.sweak.unlockmaster.presentation.common.Screen.Companion.KEY_IS_LAUNCHED_FROM_SETTINGS
import com.sweak.unlockmaster.presentation.common.Screen.Companion.KEY_IS_UPDATING_EXISTING_SCREEN_TIME_LIMIT
import com.sweak.unlockmaster.presentation.common.Screen.Companion.KEY_IS_UPDATING_EXISTING_UNLOCK_LIMIT
import com.sweak.unlockmaster.presentation.common.components.OnResume
import com.sweak.unlockmaster.presentation.common.theme.UnlockMasterTheme
import com.sweak.unlockmaster.presentation.daily_wrap_up.DailyWrapUpScreen
import com.sweak.unlockmaster.presentation.introduction.background_work.WorkInBackgroundScreen
import com.sweak.unlockmaster.presentation.introduction.introduction.IntroductionScreen
import com.sweak.unlockmaster.presentation.introduction.limit_setup.screen_time.ScreenTimeLimitSetupScreen
import com.sweak.unlockmaster.presentation.introduction.limit_setup.unlock.UnlockLimitSetupScreen
import com.sweak.unlockmaster.presentation.introduction.setup_complete.SetupCompleteScreen
import com.sweak.unlockmaster.presentation.introduction.welcome.WelcomeScreen
import com.sweak.unlockmaster.presentation.main.home.HomeScreen
import com.sweak.unlockmaster.presentation.main.screen_time.ScreenTimeScreen
import com.sweak.unlockmaster.presentation.main.statistics.StatisticsScreen
import com.sweak.unlockmaster.presentation.settings.SettingsScreen
import com.sweak.unlockmaster.presentation.settings.application_blocked.ApplicationBlockedScreen
import com.sweak.unlockmaster.presentation.settings.daily_wrap_up_settings.DailyWrapUpSettingsScreen
import com.sweak.unlockmaster.presentation.settings.data_backup.DataBackupScreen
import com.sweak.unlockmaster.presentation.settings.mobilizing_notifications.MobilizingNotificationsScreen
import com.sweak.unlockmaster.presentation.settings.user_interface_theme.UserInterfaceThemeScreen
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
    lateinit var scheduleDailyWrapUpNotificationsUseCase: ScheduleDailyWrapUpNotificationsUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val uiThemeMode by userSessionRepository.getUiThemeModeFlow().collectAsState(
                initial = UiThemeMode.SYSTEM
            )

            UnlockMasterTheme(uiThemeMode = uiThemeMode) {
                // The colorScheme of the MaterialTheme might have been manually changed by the user
                // and we have to account for that by taking care of status bar color change:
                window.apply {
                    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    statusBarColor = MaterialTheme.colorScheme.primary.toArgb()
                }

                val navController = rememberNavController()

                OnResume {
                    val dailyWrapUpDayMillis =
                        intent.getLongExtra(EXTRA_DAILY_WRAP_UP_DAY_MILLIS, 0)

                    if (intent.getBooleanExtra(EXTRA_SHOW_DAILY_WRAP_UP_SCREEN, false) &&
                        dailyWrapUpDayMillis != 0L
                    ) {
                        // Clear the intent to avoid duplicate daily wrap-up screens showing:
                        intent = intent.apply {
                            removeExtra(EXTRA_DAILY_WRAP_UP_DAY_MILLIS)
                            removeExtra(EXTRA_SHOW_DAILY_WRAP_UP_SCREEN)
                        }

                        navController.navigate(
                            Screen.DailyWrapUpScreen.withArguments(dailyWrapUpDayMillis.toString())
                        )
                    }
                }

                // Fix for blank screen issue on Xiaomi devices:
                // https://issuetracker.google.com/issues/227926002
                ScaffoldDefaults.contentWindowInsets

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
                        route = Screen.ScreenTimeLimitSetupScreen.route
                                + "/{$KEY_IS_UPDATING_EXISTING_SCREEN_TIME_LIMIT}",
                        arguments = listOf(
                            navArgument(KEY_IS_UPDATING_EXISTING_SCREEN_TIME_LIMIT) {
                                type = NavType.BoolType
                                nullable = false
                            }
                        )
                    ) {
                        ScreenTimeLimitSetupScreen(
                            navController = navController,
                            isUpdatingExistingScreenTimeLimit = it.arguments?.getBoolean(
                                KEY_IS_UPDATING_EXISTING_SCREEN_TIME_LIMIT
                            ) ?: true
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
                                    if (userSessionRepository.isIntroductionFinished()) {
                                        return@launch
                                    }

                                    // We're adding an unlock event for the data to be coherent as
                                    // the next lock event that will be caught has to have
                                    // a corresponding unlock event:
                                    addUnlockEventUseCase()
                                    addScreenOnEventUseCase()

                                    userSessionRepository.setIntroductionFinished()
                                    startUnlockMasterService()
                                    scheduleDailyWrapUpNotificationsUseCase()
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
                        HomeScreen(
                            navController = navController,
                            onBack = { finish() }
                        )
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

                    composable(route = Screen.DailyWrapUpSettingsScreen.route) {
                        DailyWrapUpSettingsScreen(navController = navController)
                    }

                    composable(
                        route = Screen.DailyWrapUpScreen.route + "/{$KEY_DAILY_WRAP_UP_DAY_MILLIS}",
                        arguments = listOf(
                            navArgument(KEY_DAILY_WRAP_UP_DAY_MILLIS) {
                                type = NavType.LongType
                                nullable = false
                            }
                        )
                    ) {
                        DailyWrapUpScreen(
                            navController = navController,
                            dailyWrapUpDayTimeInMillis =
                            it.arguments?.getLong(KEY_DAILY_WRAP_UP_DAY_MILLIS)
                                ?: System.currentTimeMillis()
                        )
                    }

                    composable(route = Screen.ApplicationBlockedScreen.route) {
                        ApplicationBlockedScreen(navController = navController)
                    }

                    composable(route = Screen.UserInterfaceThemeScreen.route) {
                        UserInterfaceThemeScreen(navController = navController)
                    }

                    composable(route = Screen.DataBackupScreen.route) {
                        DataBackupScreen(navController = navController)
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}