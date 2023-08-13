package com.sweak.unlockmaster.domain.use_case.daily_wrap_up

import com.sweak.unlockmaster.domain.model.DailyWrapUpsNotificationsTime
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import javax.inject.Inject

class GetDailyWrapUpsNotificationsTimeUseCase @Inject constructor(
    private val userSessionRepository: UserSessionRepository
) {
    suspend operator fun invoke(): DailyWrapUpsNotificationsTime {
        val notificationTimeInMinutesAfterMidnight =
            userSessionRepository.getDailyWrapUpsNotificationsTimeInMinutesAfterMidnight()

        return DailyWrapUpsNotificationsTime(
            hourOfDay = notificationTimeInMinutesAfterMidnight / 60,
            minute = notificationTimeInMinutesAfterMidnight % 60
        )
    }
}