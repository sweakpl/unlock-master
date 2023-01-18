package com.sweak.unlockmaster.presentation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.ui.theme.UnlockMasterTheme
import com.sweak.unlockmaster.presentation.introduction.background_work.WorkInBackgroundScreen
import com.sweak.unlockmaster.presentation.introduction.introduction.IntroductionScreen
import com.sweak.unlockmaster.presentation.introduction.limit_setup.UnlockLimitSetupScreen
import com.sweak.unlockmaster.presentation.introduction.setup_complete.SetupCompleteScreen
import com.sweak.unlockmaster.presentation.introduction.welcome.WelcomeScreen
import com.sweak.unlockmaster.presentation.unlock_counter_service.UnlockMasterService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UnlockMasterTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.WelcomeScreen.route
                ) {
                    composable(route = Screen.WelcomeScreen.route) {
                        WelcomeScreen(navController = navController)
                    }

                    composable(route = Screen.IntroductionScreen.route) {
                        IntroductionScreen(navController = navController)
                    }

                    composable(route = Screen.UnlockLimitSetupScreen.route) {
                        UnlockLimitSetupScreen(navController = navController)
                    }

                    composable(route = Screen.WorkInBackgroundScreen.route) {
                        WorkInBackgroundScreen(
                            navController = navController,
                            onWorkInBackgroundAllowed = { startUnlockMasterService() }
                        )
                    }

                    composable(route = Screen.SetupCompleteScreen.route) {
                        SetupCompleteScreen(navController = navController)
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