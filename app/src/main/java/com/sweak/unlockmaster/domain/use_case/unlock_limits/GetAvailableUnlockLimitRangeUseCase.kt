package com.sweak.unlockmaster.domain.use_case.unlock_limits

import com.sweak.unlockmaster.domain.UNLOCK_LIMIT_LOWER_BOUND
import com.sweak.unlockmaster.domain.UNLOCK_LIMIT_UPPER_BOUND
import javax.inject.Inject

class GetAvailableUnlockLimitRangeUseCase @Inject constructor() {

    operator fun invoke(): IntRange {
        return IntRange(
            start = UNLOCK_LIMIT_LOWER_BOUND,
            endInclusive = UNLOCK_LIMIT_UPPER_BOUND
        )
    }
}