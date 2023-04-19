package com.sweak.unlockmaster.domain.use_case.unlock_limits

import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.repository.UnlockLimitsRepository
import javax.inject.Inject

class DeleteUnlockLimitForTomorrowUseCase @Inject constructor(
    private val unlockLimitsRepository: UnlockLimitsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke() {
        unlockLimitsRepository.deleteUnlockLimitWithApplianceTime(
            limitApplianceTimeInMillis = timeRepository.getTomorrowBeginningTimeInMillis()
        )
    }
}