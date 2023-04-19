package com.sweak.unlockmaster.domain.use_case.screen_time

import javax.inject.Inject

class GetScreenTimeDurationForGivenDayUseCase @Inject constructor() {
    suspend operator fun invoke(dayTimeInMillis: Long): Long {
        return 4500000
    }
}