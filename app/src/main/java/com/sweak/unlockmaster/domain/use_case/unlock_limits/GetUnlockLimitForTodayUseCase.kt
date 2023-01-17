package com.sweak.unlockmaster.domain.use_case.unlock_limits

import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.repository.UnlockLimitsRepository
import javax.inject.Inject

class GetUnlockLimitForTodayUseCase @Inject constructor(
    private val unlockLimitsRepository: UnlockLimitsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke(): Int? {
        return unlockLimitsRepository.getCurrentUnlockLimit(
            currentTimeInMillis = timeRepository.getCurrentTimeInMillis()
        )
    }
}