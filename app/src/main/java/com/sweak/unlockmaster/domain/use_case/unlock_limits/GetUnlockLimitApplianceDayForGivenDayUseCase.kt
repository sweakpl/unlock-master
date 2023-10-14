package com.sweak.unlockmaster.domain.use_case.unlock_limits

import com.sweak.unlockmaster.domain.repository.UnlockLimitsRepository
import javax.inject.Inject

class GetUnlockLimitApplianceDayForGivenDayUseCase @Inject constructor(
    private val unlockLimitsRepository: UnlockLimitsRepository
) {
    suspend operator fun invoke(dayTimeInMillis: Long): Long? {
        return unlockLimitsRepository.getUnlockLimitActiveAtTime(
            timeInMillis = dayTimeInMillis
        )?.limitApplianceTimeInMillis
    }
}