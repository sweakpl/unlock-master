package com.sweak.unlockmaster.domain.use_case.mobilizing_notifications

import com.sweak.unlockmaster.domain.DEFAULT_MOBILIZING_NOTIFICATIONS_FREQUENCY_PERCENTAGE
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import javax.inject.Inject

class GetMobilizingNotificationsFrequencyPercentage @Inject constructor(
    private val userSessionRepository: UserSessionRepository
) {
    suspend operator fun invoke(): Int {
        return userSessionRepository.getMobilizingNotificationsFrequencyPercentage()
            ?: DEFAULT_MOBILIZING_NOTIFICATIONS_FREQUENCY_PERCENTAGE
    }
}