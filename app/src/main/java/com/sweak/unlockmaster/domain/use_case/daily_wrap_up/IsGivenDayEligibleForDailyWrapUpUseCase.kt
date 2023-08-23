package com.sweak.unlockmaster.domain.use_case.daily_wrap_up

import com.sweak.unlockmaster.domain.use_case.unlock_events.GetUnlockEventsCountForGivenDayUseCase
import javax.inject.Inject

class IsGivenDayEligibleForDailyWrapUpUseCase @Inject constructor(
    private val getUnlockEventsCountForGivenDayUseCase: GetUnlockEventsCountForGivenDayUseCase
) {
    suspend operator fun invoke(dailyWrapUpDayMillis: Long): Boolean {
        val unlocksCountForGivenDay = getUnlockEventsCountForGivenDayUseCase(dailyWrapUpDayMillis)

        return unlocksCountForGivenDay > 0
    }
}