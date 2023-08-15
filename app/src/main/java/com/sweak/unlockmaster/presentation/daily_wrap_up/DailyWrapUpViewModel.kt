package com.sweak.unlockmaster.presentation.daily_wrap_up

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class DailyWrapUpViewModel @Inject constructor(): ViewModel() {

    var state by mutableStateOf(DailyWrapUpScreenState())

    init {
        viewModelScope.launch {
            delay(500)

            state = state.copy(
                isInitializing = false,
                screenUnlocksPreviewData = DailyWrapUpCriterionPreviewType.ScreenUnlocks(
                    21,
                    DailyWrapUpCriterionPreviewType.Progress.REGRESS
                ),
                screenTimePreviewData = DailyWrapUpCriterionPreviewType.ScreenTime(
                    Duration(4500000, Duration.DisplayPrecision.MINUTES),
                    DailyWrapUpCriterionPreviewType.Progress.IMPROVEMENT
                ),
                unlockLimitPreviewData = DailyWrapUpCriterionPreviewType.UnlockLimit(30, true),
                screenOnEventsPreviewData = DailyWrapUpCriterionPreviewType.ScreenOnEvents(
                    49,
                    DailyWrapUpCriterionPreviewType.Progress.STABLE
                ),
                screenUnlocksDetailsData = DailyWrapUpScreenUnlocksDetailsData(
                    screenUnlocksCount = 21,
                    yesterdayDifference = 3,
                    weekBeforeDifference = -1
                ),
                screenTimeDetailsData = DailyWrapUpScreenTimeDetailsData(
                    screenTimeDuration = Duration(
                        4500000,
                        Duration.DisplayPrecision.MINUTES
                    ),
                    yesterdayDifference = Duration(
                        -780000,
                        Duration.DisplayPrecision.MINUTES
                    ),
                    weekBeforeDifference = Duration(
                        420000,
                        Duration.DisplayPrecision.MINUTES
                    )
                ),
                unlockLimitDetailsData = DailyWrapUpUnlockLimitDetailsData(
                    unlockLimit = 30,
                    suggestedUnlockLimit = 29,
                    isSuggestedUnlockLimitApplied = false,
                    isLimitSignificantlyExceeded = false
                ),
                screenOnEventsDetailsData = DailyWrapUpScreenOnEventsDetailsData(
                    screenOnEventsCount = 49,
                    yesterdayDifference = 0,
                    weekBeforeDifference = -3,
                    isManyMoreScreenOnEventsThanUnlocks = false
                )
            )
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