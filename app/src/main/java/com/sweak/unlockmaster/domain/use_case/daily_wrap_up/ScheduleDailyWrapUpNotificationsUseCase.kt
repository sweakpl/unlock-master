package com.sweak.unlockmaster.domain.use_case.daily_wrap_up

import com.sweak.unlockmaster.domain.management.UnlockMasterAlarmManager
import javax.inject.Inject

class ScheduleDailyWrapUpNotificationsUseCase @Inject constructor(
    private val unlockMasterAlarmManager: UnlockMasterAlarmManager,
    private val getDailyWrapUpNotificationsTimeUseCase: GetDailyWrapUpNotificationsTimeUseCase
) {
    suspend operator fun invoke() {
        unlockMasterAlarmManager.scheduleNewDailyWrapUpNotifications(
            getDailyWrapUpNotificationsTimeUseCase()
        )
    }
}