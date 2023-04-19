package com.sweak.unlockmaster.domain.use_case.unlock_limits

import com.sweak.unlockmaster.domain.model.UnlockLimit
import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.repository.UnlockLimitsRepository
import javax.inject.Inject

class AddOrUpdateUnlockLimitForTomorrowUseCase @Inject constructor(
    private val unlockLimitsRepository: UnlockLimitsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke(limitAmount: Int) {
        val tomorrowBeginningTimeInMillis = timeRepository.getTomorrowBeginningTimeInMillis()
        val currentUnlockLimit = unlockLimitsRepository.getUnlockLimitActiveAtTime(
            timeInMillis = timeRepository.getCurrentTimeInMillis()
        )
        val unlockLimitForTomorrow = unlockLimitsRepository.getUnlockLimitWithApplianceTime(
            limitApplianceTimeInMillis = tomorrowBeginningTimeInMillis
        )

        if (unlockLimitForTomorrow == null) {
            if (currentUnlockLimit?.limitAmount == limitAmount) {
                return
            }

            unlockLimitsRepository.addUnlockLimit(
                unlockLimit = UnlockLimit(
                    limitApplianceTimeInMillis = tomorrowBeginningTimeInMillis,
                    limitAmount = limitAmount
                )
            )
        } else {
            if (currentUnlockLimit?.limitAmount == limitAmount) {
                unlockLimitsRepository.deleteUnlockLimitWithApplianceTime(
                    limitApplianceTimeInMillis = tomorrowBeginningTimeInMillis
                )
                return
            }

            unlockLimitsRepository.updateUnlockLimit(
                unlockLimit = UnlockLimit(
                    limitApplianceTimeInMillis = tomorrowBeginningTimeInMillis,
                    limitAmount = limitAmount
                )
            )
        }
    }
}