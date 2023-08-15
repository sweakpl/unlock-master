package com.sweak.unlockmaster.domain.use_case.daily_wrap_up

import com.sweak.unlockmaster.domain.model.DailyWrapUpNotificationsTime
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import javax.inject.Inject

class SetDailyWrapUpNotificationsTimeUseCase @Inject constructor(
    private val userSessionRepository: UserSessionRepository
) {
    suspend operator fun invoke(dailyWrapUpNotificationsTime: DailyWrapUpNotificationsTime) {
        val notificationTimeInMinutesAfterMidnight =
            dailyWrapUpNotificationsTime.hourOfDay * 60 + dailyWrapUpNotificationsTime.minute

        userSessionRepository.setDailyWrapUpNotificationsTimeInMinutesAfterMidnight(
            minutes = notificationTimeInMinutesAfterMidnight
        )
    }
}