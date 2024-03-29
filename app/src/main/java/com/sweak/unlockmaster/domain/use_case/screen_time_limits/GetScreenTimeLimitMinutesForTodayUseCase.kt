package com.sweak.unlockmaster.domain.use_case.screen_time_limits

import com.sweak.unlockmaster.domain.DEFAULT_SCREEN_TIME_LIMIT_MINUTES
import com.sweak.unlockmaster.domain.repository.ScreenTimeLimitsRepository
import com.sweak.unlockmaster.domain.repository.TimeRepository
import javax.inject.Inject

class GetScreenTimeLimitMinutesForTodayUseCase @Inject constructor(
    private val screenTimeLimitsRepository: ScreenTimeLimitsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke(): Int {
        return screenTimeLimitsRepository.getScreenTimeLimitActiveAtTime(
            timeInMillis = timeRepository.getCurrentTimeInMillis()
        )?.limitAmountMinutes ?: DEFAULT_SCREEN_TIME_LIMIT_MINUTES
    }
}