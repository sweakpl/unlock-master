package com.sweak.unlockmaster.presentation.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sweak.unlockmaster.domain.use_case.daily_wrap_ups.ScheduleDailyWrapUpsNotificationsUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class TimePreferencesChangeReceiver : BroadcastReceiver() {

    @Inject
    lateinit var scheduleDailyWrapUpsNotificationsUseCase: ScheduleDailyWrapUpsNotificationsUseCase

    private val intentActionsToFilter = listOf(
        Intent.ACTION_TIME_CHANGED,
        Intent.ACTION_DATE_CHANGED,
        Intent.ACTION_TIMEZONE_CHANGED
    )

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action in intentActionsToFilter) {
            runBlocking {
                scheduleDailyWrapUpsNotificationsUseCase()
            }
        }
    }
}