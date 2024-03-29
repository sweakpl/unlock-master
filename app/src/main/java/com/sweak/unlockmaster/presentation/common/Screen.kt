package com.sweak.unlockmaster.presentation.common

sealed class Screen(val route: String) {
    data object WelcomeScreen : Screen("welcome_screen")
    data object IntroductionScreen : Screen("introduction_screen")
    data object UnlockLimitSetupScreen : Screen("unlock_limit_setup_screen")
    data object ScreenTimeLimitSetupScreen : Screen("screen_time_limit_setup_screen")
    data object WorkInBackgroundScreen : Screen("work_in_background_screen")
    data object SetupCompleteScreen : Screen("setup_complete_screen")
    data object HomeScreen : Screen("home_screen")
    data object SettingsScreen : Screen("settings_screen")
    data object ScreenTimeScreen : Screen("screen_time_screen")
    data object StatisticsScreen : Screen("statistics_screen")
    data object MobilizingNotificationsScreen : Screen("mobilizing_notifications_screen")
    data object DailyWrapUpSettingsScreen : Screen("daily_wrap_ups_setting_screen")
    data object DailyWrapUpScreen : Screen("daily_wrap_up_screen")
    data object ApplicationBlockedScreen : Screen("application_blocked_screen")
    data object UserInterfaceThemeScreen : Screen("user_interface_theme_screen")
    data object DataBackupScreen : Screen("data_backup_screen")

    fun withArguments(vararg arguments: String): String {
        return buildString {
            append(route)
            arguments.forEach { argument ->
                append("/$argument")
            }
        }
    }

    companion object {
        const val KEY_IS_UPDATING_EXISTING_UNLOCK_LIMIT = "isUpdatingExistingUnlockLimit"
        const val KEY_IS_UPDATING_EXISTING_SCREEN_TIME_LIMIT = "isUpdatingExistingScreenTimeLimit"
        const val KEY_DISPLAYED_SCREEN_TIME_DAY_MILLIS = "displayedScreenTimeDayMillis"
        const val KEY_IS_LAUNCHED_FROM_SETTINGS = "isLaunchedFromSettings"
        const val KEY_DAILY_WRAP_UP_DAY_MILLIS = "dailyWrapUpDayMillis"
    }
}