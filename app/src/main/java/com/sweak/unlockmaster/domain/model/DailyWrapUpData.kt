package com.sweak.unlockmaster.domain.model

data class DailyWrapUpData(
    val screenUnlocksData: ScreenUnlocksData,
    val screenTimeData: ScreenTimeData,
    val unlockLimitData: UnlockLimitData,
    val screenOnData: ScreenOnData
) {
    data class ScreenUnlocksData(
        val todayUnlocksCount: Int,
        val yesterdayUnlocksCount: Int?,
        val lastWeekUnlocksCount: Int?,
        val progress: Progress
    )

    data class ScreenTimeData(
        val todayScreenTimeDurationMillis: Long,
        val yesterdayScreenTimeDurationMillis: Long?,
        val lastWeekScreenTimeDurationMillis: Long?,
        val progress: Progress
    )

    data class UnlockLimitData(
        val todayUnlockLimit: Int,
        val tomorrowUnlockLimit: Int,
        val recommendedUnlockLimit: Int?,
        val isLimitSignificantlyExceeded: Boolean
    )

    data class ScreenOnData(
        val todayScreenOnsCount: Int,
        val yesterdayScreenOnsCount: Int?,
        val lastWeekScreenOnsCount: Int?,
        val progress: Progress,
        val isManyMoreScreenOnsThanUnlocks: Boolean
    )

    enum class Progress {
        IMPROVEMENT, REGRESS, STABLE
    }
}
