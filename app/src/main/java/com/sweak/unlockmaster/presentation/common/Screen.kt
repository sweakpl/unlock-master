package com.sweak.unlockmaster.presentation.common

sealed class Screen(val route: String) {
    object WelcomeScreen: Screen("welcome_screen")
    object IntroductionScreen: Screen("introduction_screen")
    object UnlockLimitSetupScreen: Screen("unlock_limit_setup_screen")
    object WorkInBackgroundScreen: Screen("work_in_background_screen")
    object SetupCompleteScreen: Screen("setup_complete_screen")
    object HomeScreen: Screen("home_screen")
    object ScreenTimeScreen : Screen("screen_time_screen")
    object StatisticsScreen : Screen("statistics_screen")

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
        const val KEY_DISPLAYED_SCREEN_TIME_DAY_MILLIS = "displayedScreenTimeDayMillis"
    }
}