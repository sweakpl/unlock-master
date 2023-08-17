package com.sweak.unlockmaster.domain.use_case.daily_wrap_up

import com.sweak.unlockmaster.domain.model.DailyWrapUpData
import javax.inject.Inject

class GetDailyWrapUpDataUseCase @Inject constructor() {
    suspend operator fun invoke(dailyWrapUpDayMillis: Long): DailyWrapUpData {
        return DailyWrapUpData(
            screenUnlocksData = DailyWrapUpData.ScreenUnlocksData(
                todayUnlocksCount = 21,
                yesterdayUnlocksCount = 18,
                lastWeekUnlocksCount = 22,
                progress = DailyWrapUpData.Progress.REGRESS
            ),
            screenTimeData = DailyWrapUpData.ScreenTimeData(
                todayScreenTimeDurationMillis = 4500000L,
                yesterdayScreenTimeDurationMillis = 5280000L,
                lastWeekScreenTimeDurationMillis = 4080000L,
                progress = DailyWrapUpData.Progress.IMPROVEMENT
            ),
            unlockLimitData = DailyWrapUpData.UnlockLimitData(
                todayUnlockLimit = 30,
                recommendedUnlockLimit = 29,
                isLimitSignificantlyExceeded = false
            ),
            screenOnData = DailyWrapUpData.ScreenOnData(
                todayScreenOnsCount = 49,
                yesterdayScreenOnsCount = 49,
                lastWeekScreenOnsCount = 52,
                progress = DailyWrapUpData.Progress.STABLE,
                isManyMoreScreenOnsThanUnlocks = false
            ),
            haveAllDailyWrapUpFeaturesBeenDiscovered = true
        )
    }
}