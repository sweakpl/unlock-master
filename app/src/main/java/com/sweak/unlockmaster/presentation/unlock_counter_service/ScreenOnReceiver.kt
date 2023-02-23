package com.sweak.unlockmaster.presentation.unlock_counter_service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ScreenOnReceiver : BroadcastReceiver() {

    var onScreenOn: (() -> Unit)? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(Intent.ACTION_SCREEN_ON)) {
            onScreenOn?.invoke()
        }
    }
}