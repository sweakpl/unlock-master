package com.sweak.unlockmaster.domain.use_case.screen_time_limits

import com.sweak.unlockmaster.domain.model.ScreenTimeLimit
import com.sweak.unlockmaster.domain.repository.ScreenTimeLimitsRepository
import com.sweak.unlockmaster.domain.repository.TimeRepository
import javax.inject.Inject

class AddOrUpdateScreenTimeLimitForTomorrowUseCase @Inject constructor(
    private val screenTimeLimitsRepository: ScreenTimeLimitsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke(limitAmountMinutes: Int) {
        val tomorrowBeginningTimeInMillis = timeRepository.getTomorrowBeginningTimeInMillis()
        val currentScreenTimeLimit = screenTimeLimitsRepository.getScreenTimeLimitActiveAtTime(
            timeInMillis = timeRepository.getCurrentTimeInMillis()
        )
        val screenTimeLimitForTomorrow =
            screenTimeLimitsRepository.getScreenTimeLimitWithApplianceTime(
                limitApplianceTimeInMillis = tomorrowBeginningTimeInMillis
            )
        val newScreenTimeLimit = ScreenTimeLimit(
            limitApplianceTimeInMillis = tomorrowBeginningTimeInMillis,
            limitAmountMinutes = limitAmountMinutes
        )

        if (screenTimeLimitForTomorrow == null) {
            if (currentScreenTimeLimit?.limitAmountMinutes == limitAmountMinutes) {
                return
            }

            screenTimeLimitsRepository.addScreenTimeLimit(screenTimeLimit = newScreenTimeLimit)
        } else {
            if (currentScreenTimeLimit?.limitAmountMinutes == limitAmountMinutes) {
                screenTimeLimitsRepository.deleteScreenTimeLimitWithApplianceTime(
                    limitApplianceTimeInMillis = tomorrowBeginningTimeInMillis
                )
                return
            }

            screenTimeLimitsRepository.updateScreenTimeLimit(screenTimeLimit = newScreenTimeLimit)
        }
    }
}