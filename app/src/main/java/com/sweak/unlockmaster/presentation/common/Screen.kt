package com.sweak.unlockmaster.presentation.common

sealed class Screen(val route: String) {
    object WelcomeScreen: Screen("welcome_screen")
    object IntroductionScreen: Screen("introduction_screen")
    object UnlockLimitSetupScreen: Screen("unlock_limit_setup_screen")
    object WorkInBackgroundScreen: Screen("work_in_background_screen")
}
