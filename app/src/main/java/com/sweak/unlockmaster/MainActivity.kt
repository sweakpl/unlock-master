package com.sweak.unlockmaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import com.sweak.unlockmaster.ui.theme.UnlockMasterTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UnlockMasterTheme {
                Text(text = "Hello world!")
            }
        }
    }
}