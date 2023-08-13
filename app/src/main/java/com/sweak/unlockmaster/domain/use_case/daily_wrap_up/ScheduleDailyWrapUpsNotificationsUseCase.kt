package com.sweak.unlockmaster.domain.use_case.daily_wrap_up

import com.sweak.unlockmaster.domain.management.UnlockMasterAlarmManager
import javax.inject.Inject

class ScheduleDailyWrapUpsNotificationsUseCase @Inject constructor(
    private val unlockMasterAlarmManager: UnlockMasterAlarmManager,
    private val getDailyWrapUpsNotificationsTime: GetDailyWrapUpsNotificationsTimeUseCase
) {
    suspend operator fun invoke() {
        unlockMasterAlarmManager.scheduleNewDailyWrapUpsNotifications(
            getDailyWrapUpsNotificationsTime()
        )
    }
}