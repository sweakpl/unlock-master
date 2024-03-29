package com.sweak.unlockmaster.domain.use_case.screen_time_limits

import com.sweak.unlockmaster.domain.repository.ScreenTimeLimitsRepository
import com.sweak.unlockmaster.domain.repository.TimeRepository
import javax.inject.Inject

class GetScreenTimeLimitMinutesForTomorrowUseCase @Inject constructor(
    private val screenTimeLimitsRepository: ScreenTimeLimitsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke(): Int? {
        return screenTimeLimitsRepository.getScreenTimeLimitWithApplianceTime(
            limitApplianceTimeInMillis = timeRepository.getTomorrowBeginningTimeInMillis()
        )?.limitAmountMinutes
    }
}