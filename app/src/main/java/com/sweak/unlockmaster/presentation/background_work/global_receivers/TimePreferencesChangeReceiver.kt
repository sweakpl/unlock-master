package com.sweak.unlockmaster.presentation.background_work.global_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import com.sweak.unlockmaster.domain.use_case.daily_wrap_up.ScheduleDailyWrapUpNotificationsUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class TimePreferencesChangeReceiver : BroadcastReceiver() {

    @Inject
    lateinit var scheduleDailyWrapUpNotificationsUseCase: ScheduleDailyWrapUpNotificationsUseCase

    @Inject
    lateinit var userSessionRepository: UserSessionRepository

    private val intentActionsToFilter = listOf(
        Intent.ACTION_TIME_CHANGED,
        Intent.ACTION_DATE_CHANGED,
        Intent.ACTION_TIMEZONE_CHANGED
    )

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action in intentActionsToFilter) {
            runBlocking {
                if (userSessionRepository.isIntroductionFinished()) {
                    scheduleDailyWrapUpNotificationsUseCase()
                }
            }
        }
    }
}