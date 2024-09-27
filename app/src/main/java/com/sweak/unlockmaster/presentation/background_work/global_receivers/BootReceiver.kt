package com.sweak.unlockmaster.presentation.background_work.global_receivers

import android.app.ActivityManager
import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                val activityManager =
                    context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

                activityManager.getHistoricalProcessStartReasons(1).firstOrNull()?.let {
                    // On Android 15+ ACTION_BOOT_COMPLETED is delivered on post-force-close launch.
                    // We detect that and do not act as if an actual boot happened and just return:
                    if (it.wasForceStopped()) return
                }
            }

            runBlocking {
                if (userSessionRepository.isIntroductionFinished() &&
                    !keyguardManager.isKeyguardLocked &&
                    !userSessionRepository.isUnlockCounterPaused()
                ) {
                    addScreenOnEventUseCase()
                    addUnlockEventUseCase()
                    scheduleDailyWrapUpNotificationsUseCase()
                    userSessionRepository.setShouldShowUnlockMasterBlockedWarning(false)
                }
            }
        }
    }
}