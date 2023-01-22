package com.sweak.unlockmaster.domain.use_case.unlock_limits

import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.repository.UnlockLimitsRepository
import javax.inject.Inject

class AddOrUpdateUnlockLimitForTomorrowUseCase @Inject constructor(
    private val unlockLimitsRepository: UnlockLimitsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke(limitAmount: Int) {
        val tomorrowBeginningTimeInMillis = timeRepository.getTomorrowBeginningTimeInMillis()
        val unlockLimitForTomorrow = unlockLimitsRepository.getUnlockLimitWithApplianceDay(
            limitApplianceDayTimeInMillis = tomorrowBeginningTimeInMillis
        )

        if (unlockLimitForTomorrow == null) {
            unlockLimitsRepository.addUnlockLimit(
                limitApplianceDayTimeInMillis = tomorrowBeginningTimeInMillis,
                limitAmount = limitAmount
            )
        } else {
            unlockLimitsRepository.updateUnlockLimit(
                limitApplianceDayTimeInMillis = tomorrowBeginningTimeInMillis,
                limitAmount = limitAmount
            )
        }
    }
}