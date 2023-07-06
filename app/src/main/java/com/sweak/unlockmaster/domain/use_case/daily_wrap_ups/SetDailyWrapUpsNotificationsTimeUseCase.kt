package com.sweak.unlockmaster.domain.use_case.daily_wrap_ups

import com.sweak.unlockmaster.domain.model.DailyWrapUpsNotificationsTime
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import javax.inject.Inject

class SetDailyWrapUpsNotificationsTimeUseCase @Inject constructor(
    private val userSessionRepository: UserSessionRepository
) {
    suspend operator fun invoke(dailyWrapUpsNotificationsTime: DailyWrapUpsNotificationsTime) {
        val notificationTimeInMinutesAfterMidnight =
            dailyWrapUpsNotificationsTime.hourOfDay * 60 + dailyWrapUpsNotificationsTime.minute

        userSessionRepository.setDailyWrapUpsNotificationsTimeInMinutesAfterMidnight(
            minutes = notificationTimeInMinutesAfterMidnight
        )
    }
}