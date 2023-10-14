package com.sweak.unlockmaster.presentation.background_work.global_receivers

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import com.sweak.unlockmaster.domain.use_case.counter_pause.IsUnlockCounterPausedUseCase
import com.sweak.unlockmaster.domain.use_case.daily_wrap_up.ScheduleDailyWrapUpNotificationsUseCase
import com.sweak.unlockmaster.domain.use_case.screen_on_events.AddScreenOnEventUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_events.AddUnlockEventUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var userSessionRepository: UserSessionRepository

    @Inject
    lateinit var addUnlockEventUseCase: AddUnlockEventUseCase

    @Inject
    lateinit var addScreenOnEventUseCase: AddScreenOnEventUseCase

    @Inject
    lateinit var isUnlockCounterPausedUseCase: IsUnlockCounterPausedUseCase

    @Inject
    lateinit var scheduleDailyWrapUpNotificationsUseCase: ScheduleDailyWrapUpNotificationsUseCase

    @Inject
    lateinit var keyguardManager: KeyguardManager

    private val intentActionsToFilter = listOf(
        "android.intent.action.BOOT_COMPLETED",
        "android.intent.action.ACTION_BOOT_COMPLETED",
        "android.intent.action.REBOOT",
        "android.intent.action.QUICKBOOT_POWERON",
        "com.htc.intent.action.QUICKBOOT_POWERON"
    )

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action in intentActionsToFilter) {
            runBlocking {
                if (!keyguardManager.isKeyguardLocked && !isUnlockCounterPausedUseCase()) {
                    addScreenOnEventUseCase()
                    addUnlockEventUseCase()
                    scheduleDailyWrapUpNotificationsUseCase()
                    userSessionRepository.setShouldShowUnlockMasterBlockedWarning(false)
                }
            }
        }
    }
}