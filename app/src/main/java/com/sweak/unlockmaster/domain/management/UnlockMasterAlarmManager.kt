package com.sweak.unlockmaster.domain.management

import com.sweak.unlockmaster.domain.model.DailyWrapUpNotificationsTime

interface UnlockMasterAlarmManager {
    fun scheduleNewDailyWrapUpNotifications(
        dailyWrapUpNotificationsTime: DailyWrapUpNotificationsTime
    )
}