package com.sweak.unlockmaster.presentation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import com.sweak.unlockmaster.domain.use_case.unlock_events.AddUnlockEventUseCase
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.ui.theme.UnlockMasterTheme
import com.sweak.unlockmaster.presentation.introduction.background_work.WorkInBackgroundScreen
import com.sweak.unlockmaster.presentation.introduction.introduction.IntroductionScreen
import com.sweak.unlockmaster.presentation.introduction.limit_setup.UnlockLimitSetupScreen
import com.sweak.unlockmaster.presentation.introduction.setup_complete.SetupCompleteScreen
import com.sweak.unlockmaster.presentation.introduction.welcome.WelcomeScreen
import com.sweak.unlockmaster.presentation.main.home.HomeScreen
import com.sweak.unlockmaster.presentation.unlock_counter_service.UnlockMasterService
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

                    composable(route = Screen.IntroductionScreen.route) {
                        IntroductionScreen(navController = navController)
                    }

                    composable(route = Screen.UnlockLimitSetupScreen.route) {
                        UnlockLimitSetupScreen(navController = navController)
                    }

                    composable(route = Screen.WorkInBackgroundScreen.route) {
                        WorkInBackgroundScreen(
                            navController = navController,
                            onWorkInBackgroundAllowed = {
                                lifecycleScope.launch {
                                    // We're adding an unlock event for the data to be coherent as
                                    // the next lock event that will be caught has to have
                                    // a corresponding unlock event:
                                    addUnlockEventUseCase()

                                    userSessionRepository.setIntroductionFinished()
                                    startUnlockMasterService()
                                }
                            }
                        )
                    }

                    composable(route = Screen.SetupCompleteScreen.route) {
                        SetupCompleteScreen(navController = navController)
                    }

                    composable(route = Screen.HomeScreen.route) {
                        HomeScreen()
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