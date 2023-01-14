package com.sweak.unlockmaster.presentation.common

sealed class Screen(val route: String) {
    object WelcomeScreen: Screen("welcome_screen")
    object IntroductionScreen: Screen("introduction_screen")
}
