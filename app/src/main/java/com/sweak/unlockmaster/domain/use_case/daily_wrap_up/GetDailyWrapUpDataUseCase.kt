package com.sweak.unlockmaster.domain.use_case.daily_wrap_up

import com.sweak.unlockmaster.domain.SCREEN_TIME_LIMIT_INTERVAL_MINUTES
import com.sweak.unlockmaster.domain.SCREEN_TIME_LIMIT_MINUTES_LOWER_BOUND
import com.sweak.unlockmaster.domain.UNLOCK_LIMIT_LOWER_BOUND
import com.sweak.unlockmaster.domain.UNLOCK_LIMIT_UPPER_BOUND
import com.sweak.unlockmaster.domain.model.DailyWrapUpData
import com.sweak.unlockmaster.domain.repository.ScreenTimeLimitsRepository
import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.repository.UnlockEventsRepository
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import com.sweak.unlockmaster.domain.toTimeInMillis
import com.sweak.unlockmaster.domain.use_case.screen_on_events.GetScreenOnEventsCountForGivenDayUseCase
import com.sweak.unlockmaster.domain.use_case.screen_time.GetScreenTimeDurationForGivenDayUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_events.GetUnlockEventsCountForGivenDayUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_limits.GetUnlockLimitAmountForGivenDayUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_limits.GetUnlockLimitApplianceDayForGivenDayUseCase
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.properties.Delegates

class GetDailyWrapUpDataUseCase @Inject constructor(
    private val userSessionRepository: UserSessionRepository,
    private val unlockEventsRepository: UnlockEventsRepository,
    private val getUnlockEventsCountForGivenDayUseCase: GetUnlockEventsCountForGivenDayUseCase,
    private val getScreenTimeDurationForGivenDayUseCase: GetScreenTimeDurationForGivenDayUseCase,
    private val getUnlockLimitAmountForGivenDayUseCase: GetUnlockLimitAmountForGivenDayUseCase,
    private val getUnlockLimitApplianceTimeOfUnlockLimitFromGivenDayUseCase: GetUnlockLimitApplianceDayForGivenDayUseCase,
    private val screenTimeLimitsRepository: ScreenTimeLimitsRepository,
    private val getScreenOnEventsCountForGivenDayUseCase: GetScreenOnEventsCountForGivenDayUseCase,
    private val timeRepository: TimeRepository
) {
    private lateinit var dailyWrapUpDateTime: ZonedDateTime
    private var todayUnlocksCount by Delegates.notNull<Int>()
    private var todayUnlockLimit by Delegates.notNull<Int>()

    suspend operator fun invoke(dailyWrapUpDayMillis: Long): DailyWrapUpData {
        dailyWrapUpDateTime = ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(dailyWrapUpDayMillis),
            ZoneId.systemDefault()
        )
        todayUnlocksCount = getUnlockEventsCountForGivenDayUseCase(
            dailyWrapUpDateTime.toTimeInMillis()
        )
        todayUnlockLimit = getUnlockLimitAmountForGivenDayUseCase(
            dailyWrapUpDateTime.toTimeInMillis()
        )

        return DailyWrapUpData(
            screenUnlocksData = getScreenUnlocksData(),
            screenTimeData = getScreenTimeData(),
            unlockLimitData = getUnlockLimitData(),
            screenTimeLimitData = getScreenTimeLimitData(),
            screenOnData = getScreenOnData()
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
        val tomorrowUnlockLimit = getUnlockLimitAmountForGivenDayUseCase(
            dailyWrapUpDateTime.plusDays(1).toTimeInMillis()
        )

        val todayBeginningTimeInMillis = timeRepository.getBeginningOfGivenDayTimeInMillis(
            dailyWrapUpDateTime.toTimeInMillis()
        )
        val todayUnlockLimitApplianceTimeInMillis =
            getUnlockLimitApplianceTimeOfUnlockLimitFromGivenDayUseCase(
                dailyWrapUpDateTime.toTimeInMillis()
            ) ?: todayBeginningTimeInMillis
        val dayInMillis = 86400000L

        val isLastAppliedUnlockLimitEnoughDaysAgoForNewRecommendation =
            todayBeginningTimeInMillis - todayUnlockLimitApplianceTimeInMillis >=
                    LAST_LIMIT_APPLIANCE_DAYS_DIFFERENCE_FOR_RECOMMENDATION * dayInMillis

        val recommendedUnlockLimit: Int?

        if (tomorrowUnlockLimit != todayUnlockLimit ||
            !isLastAppliedUnlockLimitEnoughDaysAgoForNewRecommendation ||
            todayUnlockLimit == UNLOCK_LIMIT_LOWER_BOUND
        ) {
            recommendedUnlockLimit = null
        } else {
            val lastWeekAverageUnlockCount = getLastWeekWeightedAverageUnlockCount()

            recommendedUnlockLimit = lastWeekAverageUnlockCount?.let {
                val unlocksDifference = (todayUnlockLimit - it).roundToInt()

                if (unlocksDifference >= MINIMAL_UNLOCKS_IMPROVEMENT_AMOUNT_FOR_RECOMMENDATION)
                    (todayUnlockLimit -
                            unlocksDifference / MINIMAL_UNLOCKS_IMPROVEMENT_AMOUNT_FOR_RECOMMENDATION)
                        .coerceAtLeast(UNLOCK_LIMIT_LOWER_BOUND)
                else null
            }
        }

        val isLimitSignificantlyExceeded =
            todayUnlocksCount >= todayUnlockLimit * UNLOCK_LIMIT_SIGNIFICANT_EXCEED_MULTIPLIER

        return DailyWrapUpData.UnlockLimitData(
            todayUnlockLimit = todayUnlockLimit,
            tomorrowUnlockLimit = tomorrowUnlockLimit,
            recommendedUnlockLimit = recommendedUnlockLimit,
            isLimitSignificantlyExceeded = isLimitSignificantlyExceeded
        )
    }

    private suspend fun getLastWeekWeightedAverageUnlockCount(): Float? {
        val firstUnlockEventDayBeginningInMillis =
            timeRepository.getBeginningOfGivenDayTimeInMillis(
                unlockEventsRepository.getFirstUnlockEvent()?.timeInMillis ?: return null
            )
        val lastWeekUnlockEventCounts = (0..6).map {
            val currentDayBeginningTimeInMillis =
                timeRepository.getBeginningOfGivenDayTimeInMillis(
                    dailyWrapUpDateTime.minusDays(it.toLong()).toTimeInMillis()
                )

            if (firstUnlockEventDayBeginningInMillis <= currentDayBeginningTimeInMillis) {
                getUnlockEventsCountForGivenDayUseCase(currentDayBeginningTimeInMillis)
            } else null
        }

        var weightedDivider = 0f
        var weightedSum = 0

        lastWeekUnlockEventCounts.forEachIndexed { index, unlockCount ->
            unlockCount?.let {
                val weight = if (index < 4) 1 else 2
                weightedDivider += weight
                weightedSum += unlockCount * weight
            }
        }

        return if (weightedDivider == 0f) null else weightedSum / weightedDivider
    }

    private suspend fun getScreenTimeLimitData(): DailyWrapUpData.ScreenTimeLimitData? {
        if (!userSessionRepository.isScreenTimeLimitEnabled()) {
            return null
        }

        val minuteInMillis = 60000L
        val todayScreenTimeLimitMinutes = screenTimeLimitsRepository.getScreenTimeLimitActiveAtTime(
            timeInMillis = dailyWrapUpDateTime.toTimeInMillis()
        )?.limitAmountMinutes ?: return null
        val tomorrowScreenTimeLimitMinutes = screenTimeLimitsRepository.getScreenTimeLimitActiveAtTime(
            timeInMillis = dailyWrapUpDateTime.plusDays(1).toTimeInMillis()
        )?.limitAmountMinutes ?: return null

        val todayBeginningTimeInMillis = timeRepository.getBeginningOfGivenDayTimeInMillis(
            dailyWrapUpDateTime.toTimeInMillis()
        )
        val todayScreenTimeLimitApplianceTimeInMillis =
            screenTimeLimitsRepository.getScreenTimeLimitActiveAtTime(
                dailyWrapUpDateTime.toTimeInMillis()
            )?.limitApplianceTimeInMillis ?: todayBeginningTimeInMillis
        val dayInMillis = 86400000L
        val isLastAppliedScreenTimeLimitEnoughDaysAgoForNewRecommendation =
            todayBeginningTimeInMillis - todayScreenTimeLimitApplianceTimeInMillis >=
                    LAST_LIMIT_APPLIANCE_DAYS_DIFFERENCE_FOR_RECOMMENDATION * dayInMillis

        val recommendedScreenTimeLimit: Int?

        if (tomorrowScreenTimeLimitMinutes != todayScreenTimeLimitMinutes ||
            !isLastAppliedScreenTimeLimitEnoughDaysAgoForNewRecommendation ||
            todayScreenTimeLimitMinutes == SCREEN_TIME_LIMIT_MINUTES_LOWER_BOUND
        ) {
            recommendedScreenTimeLimit = null
        } else {
            val lastWeekAverageScreenTimeMillis = getLastWeekWeightedAverageScreenTimeMillis()

            recommendedScreenTimeLimit = lastWeekAverageScreenTimeMillis?.let {
                val screenTimeDifferenceMinutes =
                    (todayScreenTimeLimitMinutes * minuteInMillis - it) / minuteInMillis

                if (screenTimeDifferenceMinutes >= MINIMAL_SCREEN_TIME_IMPROVEMENT_MINUTES_FOR_RECOMMENDATION) {
                    val screenTimeLimitDecreaseMultiplier =
                        screenTimeDifferenceMinutes / MINIMAL_SCREEN_TIME_IMPROVEMENT_MINUTES_FOR_RECOMMENDATION
                    val screenTimeLimitDecreaseMinutes =
                        (SCREEN_TIME_LIMIT_INTERVAL_MINUTES * screenTimeLimitDecreaseMultiplier)
                            .toInt()

                    (todayScreenTimeLimitMinutes - screenTimeLimitDecreaseMinutes)
                        .coerceAtLeast(SCREEN_TIME_LIMIT_MINUTES_LOWER_BOUND)
                } else null
            }
        }

        val isLimitSignificantlyExceeded = todayScreenTimeLimitMinutes >=
                todayScreenTimeLimitMinutes * SCREEN_TIME_LIMIT_SIGNIFICANT_EXCEED_MULTIPLIER

        return DailyWrapUpData.ScreenTimeLimitData(
            todayScreenTimeLimitDurationMinutes = todayScreenTimeLimitMinutes,
            tomorrowScreenTimeLimitDurationMinutes = tomorrowScreenTimeLimitMinutes,
            recommendedScreenTimeLimitDurationMinutes = recommendedScreenTimeLimit,
            isLimitSignificantlyExceeded = isLimitSignificantlyExceeded
        )
    }

    private suspend fun getLastWeekWeightedAverageScreenTimeMillis(): Long? {
        val firstUnlockEventDayBeginningInMillis =
            timeRepository.getBeginningOfGivenDayTimeInMillis(
                unlockEventsRepository.getFirstUnlockEvent()?.timeInMillis ?: return null
            )
        val lastWeekScreenTimes = (0..6).map {
            val currentDayBeginningTimeInMillis =
                timeRepository.getBeginningOfGivenDayTimeInMillis(
                    dailyWrapUpDateTime.minusDays(it.toLong()).toTimeInMillis()
                )

            if (firstUnlockEventDayBeginningInMillis <= currentDayBeginningTimeInMillis) {
                getScreenTimeDurationForGivenDayUseCase(currentDayBeginningTimeInMillis)
            } else null
        }

        var weightedDivider = 0f
        var weightedSum = 0L

        lastWeekScreenTimes.forEachIndexed { index, screenTime ->
            screenTime?.let {
                val weight = if (index < 4) 1 else 2
                weightedDivider += weight
                weightedSum += screenTime * weight
            }
        }

        return if (weightedDivider == 0f) null else (weightedSum / weightedDivider).roundToLong()
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
            if (it > todayScreenOnsCount) DailyWrapUpData.Progress.IMPROVEMENT
            else if (it < todayScreenOnsCount) DailyWrapUpData.Progress.REGRESS
            else DailyWrapUpData.Progress.STABLE
        } ?: lastWeekScreenOnsCount?.let {
            if (it > todayScreenOnsCount) DailyWrapUpData.Progress.IMPROVEMENT
            else if (it < todayScreenOnsCount) DailyWrapUpData.Progress.REGRESS
            else DailyWrapUpData.Progress.STABLE
        } ?: DailyWrapUpData.Progress.STABLE

        val manyMoreScreenOnsThanUnlocksMultiplier =
            ManyMoreScreenOnsThanUnlocksMultiplier.entries.firstOrNull {
                todayUnlockLimit in it.suitableUnlockLimitRange
            }?.multiplier

        val isManyMoreScreenOnsThanUnlocks =
            manyMoreScreenOnsThanUnlocksMultiplier?.let {
                todayScreenOnsCount > todayUnlocksCount * manyMoreScreenOnsThanUnlocksMultiplier
            } ?: false

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
        const val MINIMAL_SCREEN_TIME_IMPROVEMENT_MINUTES_FOR_RECOMMENDATION = 15
        const val UNLOCK_LIMIT_SIGNIFICANT_EXCEED_MULTIPLIER = 1.5
        const val SCREEN_TIME_LIMIT_SIGNIFICANT_EXCEED_MULTIPLIER = 1.5
        const val LAST_LIMIT_APPLIANCE_DAYS_DIFFERENCE_FOR_RECOMMENDATION = 3

        enum class ManyMoreScreenOnsThanUnlocksMultiplier(
            val multiplier: Int,
            val suitableUnlockLimitRange: IntRange
        ) {
            LENIENT(5, IntRange(UNLOCK_LIMIT_LOWER_BOUND, 20)),
            MODERATE(4, IntRange(21, 40)),
            STRICT(3, IntRange(41, UNLOCK_LIMIT_UPPER_BOUND))
        }
    }
}