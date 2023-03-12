package com.sweak.unlockmaster.presentation.unlock_counter_service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sweak.unlockmaster.domain.use_case.lock_events.AddLockEventUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class ShutdownReceiver : BroadcastReceiver() {

    @Inject
    lateinit var addLockEventUseCase: AddLockEventUseCase

    private val intentActionsToFilter = listOf(
        "android.intent.action.ACTION_SHUTDOWN",
        "android.intent.action.QUICKBOOT_POWEROFF"
    )

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action in intentActionsToFilter) {
            runBlocking { addLockEventUseCase() }
        }
    }
}