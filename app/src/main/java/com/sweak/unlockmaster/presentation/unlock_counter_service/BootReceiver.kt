package com.sweak.unlockmaster.presentation.unlock_counter_service

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sweak.unlockmaster.domain.use_case.unlock_events.AddUnlockEventUseCase
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class BootReceiver : BroadcastReceiver() {

    @Inject lateinit var addUnlockEventUseCase: AddUnlockEventUseCase

    @Inject lateinit var keyguardManager: KeyguardManager

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            if (!keyguardManager.isKeyguardLocked) {
                runBlocking { addUnlockEventUseCase() }
            }
        }
    }
}