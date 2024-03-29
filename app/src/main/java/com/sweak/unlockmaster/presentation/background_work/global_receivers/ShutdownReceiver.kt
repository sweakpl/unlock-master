package com.sweak.unlockmaster.presentation.background_work.global_receivers

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import com.sweak.unlockmaster.domain.use_case.lock_events.AddLockEventUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class ShutdownReceiver : BroadcastReceiver() {

    @Inject
    lateinit var userSessionRepository: UserSessionRepository

    @Inject
    lateinit var addLockEventUseCase: AddLockEventUseCase

    @Inject
    lateinit var keyguardManager: KeyguardManager

    private val intentActionsToFilter = listOf(
        "android.intent.action.ACTION_SHUTDOWN",
        "android.intent.action.QUICKBOOT_POWEROFF"
    )

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action in intentActionsToFilter) {
            runBlocking {
                if (userSessionRepository.isIntroductionFinished() &&
                    !keyguardManager.isKeyguardLocked &&
                    !userSessionRepository.isUnlockCounterPaused()
                ) {
                    addLockEventUseCase()
                }
            }
        }
    }
}