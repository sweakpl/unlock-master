package com.sweak.unlockmaster.domain.management

import com.sweak.unlockmaster.domain.model.DailyWrapUpsNotificationsTime

interface UnlockMasterAlarmManager {
    fun scheduleNewDailyWrapUpsNotifications(
        dailyWrapUpsNotificationsTime: DailyWrapUpsNotificationsTime
    )
}