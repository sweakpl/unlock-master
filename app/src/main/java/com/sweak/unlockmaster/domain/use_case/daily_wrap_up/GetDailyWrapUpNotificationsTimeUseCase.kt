package com.sweak.unlockmaster.domain.use_case.daily_wrap_up

import com.sweak.unlockmaster.domain.model.DailyWrapUpNotificationsTime
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import javax.inject.Inject

class GetDailyWrapUpNotificationsTimeUseCase @Inject constructor(
    private val userSessionRepository: UserSessionRepository
) {
    suspend operator fun invoke(): DailyWrapUpNotificationsTime {
        val notificationTimeInMinutesAfterMidnight =
            userSessionRepository.getDailyWrapUpNotificationsTimeInMinutesAfterMidnight()

        return DailyWrapUpNotificationsTime(
            hourOfDay = notificationTimeInMinutesAfterMidnight / 60,
            minute = notificationTimeInMinutesAfterMidnight % 60
        )
    }
}