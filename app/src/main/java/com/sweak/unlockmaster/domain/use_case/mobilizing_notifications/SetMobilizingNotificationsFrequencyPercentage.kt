package com.sweak.unlockmaster.domain.use_case.mobilizing_notifications

import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import javax.inject.Inject

class SetMobilizingNotificationsFrequencyPercentage @Inject constructor(
    private val userSessionRepository: UserSessionRepository
) {
    suspend operator fun invoke(percentage: Int) {
        userSessionRepository.setMobilizingNotificationsFrequencyPercentage(
            percentage = percentage
        )
    }
}