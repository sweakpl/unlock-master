package com.sweak.unlockmaster.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sweak.unlockmaster.presentation.common.ui.theme.UnlockMasterTheme
import com.sweak.unlockmaster.presentation.introduction.welcome.WelcomeScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UnlockMasterTheme {
                WelcomeScreen()
            }
        }
    }
}