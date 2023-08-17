package com.sweak.unlockmaster.presentation.daily_wrap_up

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweak.unlockmaster.domain.model.DailyWrapUpData
import com.sweak.unlockmaster.domain.use_case.daily_wrap_up.GetDailyWrapUpDataUseCase
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.util.Duration
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpCriterionPreviewType
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpScreenOnEventsDetailsData
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpScreenTimeDetailsData
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpScreenUnlocksDetailsData
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpUnlockLimitDetailsData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyWrapUpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDailyWrapUpDataUseCase: GetDailyWrapUpDataUseCase
) : ViewModel() {

    private val dailyWrapUpDayMillis: Long =
        checkNotNull(savedStateHandle[Screen.KEY_DAILY_WRAP_UP_DAY_MILLIS])

    var state by mutableStateOf(DailyWrapUpScreenState())

    init {
        viewModelScope.launch {
            delay(500)

            val dailyWrapUpData = getDailyWrapUpDataUseCase(dailyWrapUpDayMillis)

            val todayUnlocksCount = dailyWrapUpData.screenUnlocksData.todayUnlocksCount
            val todayScreenTimeDurationMillis =
                dailyWrapUpData.screenTimeData.todayScreenTimeDurationMillis
            val todayUnlockLimit = dailyWrapUpData.unlockLimitData.todayUnlockLimit
            val todayScreenOnEventsCount = dailyWrapUpData.screenOnData.todayScreenOnsCount

            state = state.copy(
                isInitializing = false,
                screenUnlocksPreviewData = DailyWrapUpCriterionPreviewType.ScreenUnlocks(
                    screenUnlocksCount = todayUnlocksCount,
                    progress = convertDomainProgressToUiProgressEnum(dailyWrapUpData.screenUnlocksData.progress)
                ),
                screenTimePreviewData = DailyWrapUpCriterionPreviewType.ScreenTime(
                    screenTimeDuration = Duration(
                        todayScreenTimeDurationMillis,
                        Duration.DisplayPrecision.MINUTES
                    ),
                    progress = convertDomainProgressToUiProgressEnum(dailyWrapUpData.screenTimeData.progress)
                ),
                unlockLimitPreviewData = DailyWrapUpCriterionPreviewType.UnlockLimit(
                    unlockLimitCount = todayUnlockLimit,
                    isSuggestionAvailable = dailyWrapUpData.unlockLimitData.recommendedUnlockLimit != null
                ),
                screenOnEventsPreviewData = DailyWrapUpCriterionPreviewType.ScreenOnEvents(
                    screenOnEventsCount = todayScreenOnEventsCount,
                    progress = convertDomainProgressToUiProgressEnum(dailyWrapUpData.screenOnData.progress)
                ),
                screenUnlocksDetailsData = DailyWrapUpScreenUnlocksDetailsData(
                    screenUnlocksCount = dailyWrapUpData.screenUnlocksData.todayUnlocksCount,
                    yesterdayDifference = dailyWrapUpData.screenUnlocksData.yesterdayUnlocksCount?.let {
                        todayUnlocksCount - it
                    },
                    weekBeforeDifference = dailyWrapUpData.screenUnlocksData.lastWeekUnlocksCount?.let {
                        todayUnlocksCount - it
                    }
                ),
                screenTimeDetailsData = DailyWrapUpScreenTimeDetailsData(
                    screenTimeDuration = Duration(
                        todayScreenTimeDurationMillis,
                        Duration.DisplayPrecision.MINUTES
                    ),
                    yesterdayDifference = dailyWrapUpData.screenTimeData.yesterdayScreenTimeDurationMillis?.let {
                        Duration(
                            todayScreenTimeDurationMillis - it,
                            Duration.DisplayPrecision.MINUTES
                        )
                    },
                    weekBeforeDifference = dailyWrapUpData.screenTimeData.lastWeekScreenTimeDurationMillis?.let {
                        Duration(
                            todayScreenTimeDurationMillis - it,
                            Duration.DisplayPrecision.MINUTES
                        )
                    }
                ),
                unlockLimitDetailsData = DailyWrapUpUnlockLimitDetailsData(
                    unlockLimit = todayUnlockLimit,
                    suggestedUnlockLimit = dailyWrapUpData.unlockLimitData.recommendedUnlockLimit,
                    isSuggestedUnlockLimitApplied = false,
                    isLimitSignificantlyExceeded = dailyWrapUpData.unlockLimitData.isLimitSignificantlyExceeded
                ),
                screenOnEventsDetailsData = DailyWrapUpScreenOnEventsDetailsData(
                    screenOnEventsCount = todayScreenOnEventsCount,
                    yesterdayDifference = dailyWrapUpData.screenOnData.yesterdayScreenOnsCount?.let {
                        todayScreenOnEventsCount - it
                    },
                    weekBeforeDifference = dailyWrapUpData.screenOnData.lastWeekScreenOnsCount?.let {
                        todayScreenOnEventsCount - it
                    },
                    isManyMoreScreenOnEventsThanUnlocks = dailyWrapUpData.screenOnData.isManyMoreScreenOnsThanUnlocks
                ),
                haveAllDailyWrapUpFeaturesBeenDiscovered = dailyWrapUpData.haveAllDailyWrapUpFeaturesBeenDiscovered
            )
        }
    }

    private fun convertDomainProgressToUiProgressEnum(
        progress: DailyWrapUpData.Progress
    ): DailyWrapUpCriterionPreviewType.Progress {
        return when (progress) {
            DailyWrapUpData.Progress.IMPROVEMENT -> DailyWrapUpCriterionPreviewType.Progress.IMPROVEMENT
            DailyWrapUpData.Progress.REGRESS -> DailyWrapUpCriterionPreviewType.Progress.REGRESS
            DailyWrapUpData.Progress.STABLE -> DailyWrapUpCriterionPreviewType.Progress.STABLE
        }
    }

    fun onEvent(event: DailyWrapUpScreenEvent) {
        when (event) {
            is DailyWrapUpScreenEvent.ScreenOnEventsInformationDialogVisible -> {
                state = state.copy(isScreenOnEventsInformationDialogVisible = event.isVisible)
            }
            DailyWrapUpScreenEvent.ApplySuggestedUnlockLimit -> {
                /* TODO: update unlock limit */
                state = state.copy(
                    unlockLimitDetailsData = state.unlockLimitDetailsData?.copy(
                        isSuggestedUnlockLimitApplied = true
                    )
                )
            }
        }
    }
}