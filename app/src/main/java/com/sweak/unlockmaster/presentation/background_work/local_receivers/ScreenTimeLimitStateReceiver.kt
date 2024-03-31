package com.sweak.unlockmaster.presentation.background_work.local_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sweak.unlockmaster.presentation.background_work.ACTION_SCREEN_TIME_LIMIT_STATE_CHANGED
import com.sweak.unlockmaster.presentation.background_work.EXTRA_IS_SCREEN_TIME_LIMIT_ENABLED

class ScreenTimeLimitStateReceiver : BroadcastReceiver() {

    var onScreenTimeLimitStateChanged: ((isEnabled: Boolean) -> Unit)? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (intent.action == ACTION_SCREEN_TIME_LIMIT_STATE_CHANGED) {
                val isScreenTimeLimitEnabled = intent.getBooleanExtra(
                    EXTRA_IS_SCREEN_TIME_LIMIT_ENABLED,
                    true
                )

                onScreenTimeLimitStateChanged?.invoke(isScreenTimeLimitEnabled)
            }
        }
    }
}