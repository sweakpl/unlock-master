package com.sweak.unlockmaster.domain.use_case.daily_wrap_up

import com.sweak.unlockmaster.domain.UNLOCK_LIMIT_LOWER_BOUND
import com.sweak.unlockmaster.domain.model.DailyWrapUpData
import com.sweak.unlockmaster.domain.toTimeInMillis
import com.sweak.unlockmaster.domain.use_case.screen_on_events.GetScreenOnEventsCountForGivenDayUseCase
import com.sweak.unlockmaster.domain.use_case.screen_time.GetScreenTimeDurationForGivenDayUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_events.GetFirstUnlockEventUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_events.GetUnlockEventsCountForGivenDayUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_limits.GetUnlockLimitAmountForGivenDayUseCase
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class GetDailyWrapUpDataUseCase @Inject constructor(
    private val getFirstUnlockEventUseCase: GetFirstUnlockEventUseCase,
    private val getUnlockEventsCountForGivenDayUseCase: GetUnlockEventsCountForGivenDayUseCase,
    private val getScreenTimeDurationForGivenDayUseCase: GetScreenTimeDurationForGivenDayUseCase,
    private val getUnlockLimitAmountForGivenDayUseCase: GetUnlockLimitAmountForGivenDayUseCase,
    private val getScreenOnEventsCountForGivenDayUseCase: GetScreenOnEventsCountForGivenDayUseCase
) {
    private lateinit var dailyWrapUpDateTime: ZonedDateTime
    private var todayUnlocksCount by Delegates.notNull<Int>()
    private var hasAppBeenUsedForAtLeastAWeek by Delegates.notNull<Boolean>()

    suspend operator fun invoke(dailyWrapUpDayMillis: Long): DailyWrapUpData {
        dailyWrapUpDateTime = ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(dailyWrapUpDayMillis),
            ZoneId.systemDefault()
        )
        todayUnlocksCount = getUnlockEventsCountForGivenDayUseCase(
            dailyWrapUpDateTime.toTimeInMillis()
        )
        hasAppBeenUsedForAtLeastAWeek = getFirstUnlockEventUseCase()?.let {
            dailyWrapUpDateTime.toTimeInMillis() - it.timeInMillis >= 604800000L // 1 week millis
        } ?: false

        return DailyWrapUpData(
            screenUnlocksData = getScreenUnlocksData(),
            screenTimeData = getScreenTimeData(),
            unlockLimitData = getUnlockLimitData(),
            screenOnData = getScreenOnData(),
            haveAllDailyWrapUpFeaturesBeenDiscovered = hasAppBeenUsedForAtLeastAWeek
        )
    }

    private suspend fun getScreenUnlocksData(): DailyWrapUpData.ScreenUnlocksData {
        val yesterdayUnlocksCount = getUnlockEventsCountForGivenDayUseCase(
            dailyWrapUpDateTime.minusDays(1).toTimeInMillis()
        ).run { if (this == 0) null else this }
        val lastWeekUnlocksCount = getUnlockEventsCountForGivenDayUseCase(
            dailyWrapUpDateTime.minusDays(7).toTimeInMillis()
        ).run { if (this == 0) null else this }

        val progress = yesterdayUnlocksCount?.let {
            if (it > todayUnlocksCount) DailyWrapUpData.Progress.IMPROVEMENT
            else if (it < todayUnlocksCount) DailyWrapUpData.Progress.REGRESS
            else DailyWrapUpData.Progress.STABLE
        } ?: lastWeekUnlocksCount?.let {
            if (it > todayUnlocksCount) DailyWrapUpData.Progress.IMPROVEMENT
            else if (it < todayUnlocksCount) DailyWrapUpData.Progress.REGRESS
            else DailyWrapUpData.Progress.STABLE
        } ?: DailyWrapUpData.Progress.STABLE

        return DailyWrapUpData.ScreenUnlocksData(
            todayUnlocksCount = todayUnlocksCount,
            yesterdayUnlocksCount = yesterdayUnlocksCount,
            lastWeekUnlocksCount = lastWeekUnlocksCount,
            progress = progress
        )
    }

    private suspend fun getScreenTimeData(): DailyWrapUpData.ScreenTimeData {
        val todayScreenTimeDurationMillis = getScreenTimeDurationForGivenDayUseCase(
            dailyWrapUpDateTime.toTimeInMillis()
        )
        val yesterdayScreenTimeDurationMillis = getScreenTimeDurationForGivenDayUseCase(
            dailyWrapUpDateTime.minusDays(1).toTimeInMillis()
        ).run { if (this == 0L) null else this }
        val lastWeekScreenTimeDurationMillis = getScreenTimeDurationForGivenDayUseCase(
            dailyWrapUpDateTime.minusDays(7).toTimeInMillis()
        ).run { if (this == 0L) null else this }

        val minuteInMillis = 60000L
        val progress = yesterdayScreenTimeDurationMillis?.let {
            with(todayScreenTimeDurationMillis - it) {
                if (this < -minuteInMillis) DailyWrapUpData.Progress.IMPROVEMENT
                else if (this > minuteInMillis) DailyWrapUpData.Progress.REGRESS
                else DailyWrapUpData.Progress.STABLE
            }
        } ?: lastWeekScreenTimeDurationMillis?.let {
            with(todayScreenTimeDurationMillis - it) {
                if (this < -minuteInMillis) DailyWrapUpData.Progress.IMPROVEMENT
                else if (this > minuteInMillis) DailyWrapUpData.Progress.REGRESS
                else DailyWrapUpData.Progress.STABLE
            }
        } ?: DailyWrapUpData.Progress.STABLE

        return DailyWrapUpData.ScreenTimeData(
            todayScreenTimeDurationMillis = todayScreenTimeDurationMillis,
            yesterdayScreenTimeDurationMillis = yesterdayScreenTimeDurationMillis,
            lastWeekScreenTimeDurationMillis = lastWeekScreenTimeDurationMillis,
            progress = progress
        )
    }

    private suspend fun getUnlockLimitData(): DailyWrapUpData.UnlockLimitData {
        val todayUnlockLimit = getUnlockLimitAmountForGivenDayUseCase(
            dailyWrapUpDateTime.toTimeInMillis()
        )
        val tomorrowUnlockLimit = getUnlockLimitAmountForGivenDayUseCase(
            dailyWrapUpDateTime.plusDays(1).toTimeInMillis()
        )

        val lastWeekAverageUnlockCount = (0..6).sumOf {
            getUnlockEventsCountForGivenDayUseCase(
                dailyWrapUpDateTime.minusDays(1).toTimeInMillis()
            )
        } / 7.0
        val recommendedUnlockLimit =
            if (!hasAppBeenUsedForAtLeastAWeek ||
                tomorrowUnlockLimit != todayUnlockLimit ||
                todayUnlockLimit == UNLOCK_LIMIT_LOWER_BOUND
            ) {
                null
            } else {
                val unlocksDifference = (todayUnlockLimit - lastWeekAverageUnlockCount).roundToInt()

                if (unlocksDifference >= MINIMAL_UNLOCKS_IMPROVEMENT_AMOUNT_FOR_RECOMMENDATION)
                    todayUnlockLimit -
                            unlocksDifference / MINIMAL_UNLOCKS_IMPROVEMENT_AMOUNT_FOR_RECOMMENDATION
                else null
            }

        val isLimitSignificantlyExceeded =
            todayUnlocksCount >= todayUnlockLimit * UNLOCK_LIMIT_SIGNIFICANT_EXCEED_MULTIPLIER

        return DailyWrapUpData.UnlockLimitData(
            todayUnlockLimit = todayUnlockLimit,
            recommendedUnlockLimit = recommendedUnlockLimit,
            isLimitSignificantlyExceeded = isLimitSignificantlyExceeded
        )
    }

    private suspend fun getScreenOnData(): DailyWrapUpData.ScreenOnData {
        val todayScreenOnsCount = getScreenOnEventsCountForGivenDayUseCase(
            dailyWrapUpDateTime.toTimeInMillis()
        )
        val yesterdayScreenOnsCount = getScreenOnEventsCountForGivenDayUseCase(
            dailyWrapUpDateTime.minusDays(1).toTimeInMillis()
        ).run { if (this == 0) null else this }
        val lastWeekScreenOnsCount = getScreenOnEventsCountForGivenDayUseCase(
            dailyWrapUpDateTime.minusDays(7).toTimeInMillis()
        ).run { if (this == 0) null else this }

        val progress = yesterdayScreenOnsCount?.let {
            if (it > todayUnlocksCount) DailyWrapUpData.Progress.IMPROVEMENT
            else if (it < todayUnlocksCount) DailyWrapUpData.Progress.REGRESS
            else DailyWrapUpData.Progress.STABLE
        } ?: lastWeekScreenOnsCount?.let {
            if (it > todayUnlocksCount) DailyWrapUpData.Progress.IMPROVEMENT
            else if (it < todayUnlocksCount) DailyWrapUpData.Progress.REGRESS
            else DailyWrapUpData.Progress.STABLE
        } ?: DailyWrapUpData.Progress.STABLE

        val isManyMoreScreenOnsThanUnlocks =
            todayScreenOnsCount > todayUnlocksCount * MANY_MORE_SCREEN_ONS_THAN_UNLOCKS_MULTIPLIER

        return DailyWrapUpData.ScreenOnData(
            todayScreenOnsCount = todayScreenOnsCount,
            yesterdayScreenOnsCount = yesterdayScreenOnsCount,
            lastWeekScreenOnsCount = lastWeekScreenOnsCount,
            progress = progress,
            isManyMoreScreenOnsThanUnlocks = isManyMoreScreenOnsThanUnlocks
        )
    }

    private companion object {
        const val MINIMAL_UNLOCKS_IMPROVEMENT_AMOUNT_FOR_RECOMMENDATION = 3
        const val UNLOCK_LIMIT_SIGNIFICANT_EXCEED_MULTIPLIER = 1.5
        const val MANY_MORE_SCREEN_ONS_THAN_UNLOCKS_MULTIPLIER = 2
    }
}