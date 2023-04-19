package com.sweak.unlockmaster.domain.use_case.unlock_limits

import com.sweak.unlockmaster.domain.DEFAULT_UNLOCK_LIMIT
import com.sweak.unlockmaster.domain.repository.UnlockLimitsRepository
import javax.inject.Inject

class GetUnlockLimitAmountForGivenDayUseCase @Inject constructor(
    private val unlockLimitsRepository: UnlockLimitsRepository
) {
    suspend operator fun invoke(dayTimeInMillis: Long): Int {
        return unlockLimitsRepository.getUnlockLimitActiveAtTime(
            timeInMillis = dayTimeInMillis
        )?.limitAmount ?: DEFAULT_UNLOCK_LIMIT
    }
}