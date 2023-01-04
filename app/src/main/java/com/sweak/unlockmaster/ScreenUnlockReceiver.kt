package com.sweak.unlockmaster

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ScreenUnlockReceiver : BroadcastReceiver() {

    var onScreenUnlock: (() -> Unit)? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(Intent.ACTION_USER_PRESENT)) {
            onScreenUnlock?.invoke()
        }
    }
}