package com.sweak.unlockmaster.domain.use_case.screen_time_limits

import com.sweak.unlockmaster.domain.model.ScreenTimeLimit
import com.sweak.unlockmaster.domain.repository.ScreenTimeLimitsRepository
import com.sweak.unlockmaster.domain.repository.TimeRepository
import javax.inject.Inject

class AddOrUpdateScreenTimeLimitForTodayUseCase @Inject constructor(
    private val screenTimeLimitsRepository: ScreenTimeLimitsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke(limitAmountMinutes: Int) {
        val latestScreenTimeLimit = screenTimeLimitsRepository.getScreenTimeLimitActiveAtTime(
            timeInMillis = timeRepository.getCurrentTimeInMillis()
        )
        val todayBeginningTimeInMillis = timeRepository.getTodayBeginningTimeInMillis()
        val newScreenTimeLimit = ScreenTimeLimit(
            limitApplianceTimeInMillis = todayBeginningTimeInMillis,
            limitAmountMinutes = limitAmountMinutes
        )

        if (latestScreenTimeLimit == null) {
            screenTimeLimitsRepository.addScreenTimeLimit(screenTimeLimit = newScreenTimeLimit)
        } else {
            if (latestScreenTimeLimit.limitApplianceTimeInMillis < todayBeginningTimeInMillis) {
                screenTimeLimitsRepository.addScreenTimeLimit(screenTimeLimit = newScreenTimeLimit)
            } else {
                screenTimeLimitsRepository.updateScreenTimeLimit(
                    screenTimeLimit = newScreenTimeLimit
                )
            }
        }
    }
}