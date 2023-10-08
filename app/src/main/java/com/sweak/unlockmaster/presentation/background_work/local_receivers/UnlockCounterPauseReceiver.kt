package com.sweak.unlockmaster.presentation.background_work.local_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sweak.unlockmaster.presentation.background_work.ACTION_UNLOCK_COUNTER_PAUSE_CHANGED
import com.sweak.unlockmaster.presentation.background_work.EXTRA_IS_UNLOCK_COUNTER_PAUSED

class UnlockCounterPauseReceiver : BroadcastReceiver() {

    var onCounterPauseChanged: ((isPaused: Boolean) -> Unit)? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (intent.action == ACTION_UNLOCK_COUNTER_PAUSE_CHANGED) {
                val isUnlockCounterPaused = intent.getBooleanExtra(
                    EXTRA_IS_UNLOCK_COUNTER_PAUSED,
                    false
                )

                onCounterPauseChanged?.invoke(isUnlockCounterPaused)
            }
        }
    }
}