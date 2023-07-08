package com.sweak.unlockmaster.presentation.background_work.global_receivers.screen_event_receivers

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