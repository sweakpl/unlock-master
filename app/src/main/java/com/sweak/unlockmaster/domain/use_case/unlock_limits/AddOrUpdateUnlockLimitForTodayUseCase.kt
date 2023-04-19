package com.sweak.unlockmaster.domain.use_case.unlock_limits

import com.sweak.unlockmaster.domain.model.UnlockLimit
import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.repository.UnlockLimitsRepository
import javax.inject.Inject

class AddOrUpdateUnlockLimitForTodayUseCase @Inject constructor(
    private val unlockLimitsRepository: UnlockLimitsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke(limitAmount: Int) {
        val latestUnlockLimit = unlockLimitsRepository.getUnlockLimitActiveAtTime(
            timeInMillis = timeRepository.getCurrentTimeInMillis()
        )
        val todayBeginningTimeInMillis = timeRepository.getTodayBeginningTimeInMillis()

        if (latestUnlockLimit == null) {
            unlockLimitsRepository.addUnlockLimit(
                unlockLimit = UnlockLimit(
                    limitApplianceTimeInMillis = todayBeginningTimeInMillis,
                    limitAmount = limitAmount
                )
            )
        } else {
            if (latestUnlockLimit.limitApplianceTimeInMillis < todayBeginningTimeInMillis) {
                unlockLimitsRepository.addUnlockLimit(
                    unlockLimit = UnlockLimit(
                        limitApplianceTimeInMillis = todayBeginningTimeInMillis,
                        limitAmount = limitAmount
                    )
                )
            } else {
                unlockLimitsRepository.updateUnlockLimit(
                    unlockLimit = UnlockLimit(
                        limitApplianceTimeInMillis = todayBeginningTimeInMillis,
                        limitAmount = limitAmount
                    )
                )
            }
        }
    }
}