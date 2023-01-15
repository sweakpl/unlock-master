package com.sweak.unlockmaster.presentation.unlock_counter_service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ScreenLockReceiver : BroadcastReceiver() {

    var onScreenLock: (() -> Unit)? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(Intent.ACTION_SCREEN_OFF)) {
            onScreenLock?.invoke()
        }
    }
}