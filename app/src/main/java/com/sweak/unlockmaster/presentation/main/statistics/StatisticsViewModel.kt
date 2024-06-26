package com.sweak.unlockmaster.presentation.main.statistics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.BarEntry
import com.sweak.unlockmaster.domain.repository.ScreenTimeLimitsRepository
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import com.sweak.unlockmaster.domain.toTimeInMillis
import com.sweak.unlockmaster.domain.use_case.screen_on_events.GetScreenOnEventsCountForGivenDayUseCase
import com.sweak.unlockmaster.domain.use_case.screen_time.GetScreenTimeDurationForGivenDayUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_events.GetAllTimeDaysToUnlockEventCountsUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_events.GetUnlockEventsCountForGivenDayUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_limits.GetUnlockLimitAmountForGivenDayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val userSessionRepository: UserSessionRepository,
    private val screenTimeLimitsRepository: ScreenTimeLimitsRepository,
    private val getAllTimeDaysToUnlockEventCountsUseCase: GetAllTimeDaysToUnlockEventCountsUseCase,
    private val getUnlockEventsCountForGivenDayUseCase: GetUnlockEventsCountForGivenDayUseCase,
    private val getUnlockLimitAmountForGivenDayUseCase: GetUnlockLimitAmountForGivenDayUseCase,
    private val getScreenOnEventsCountForGivenDayUseCase: GetScreenOnEventsCountForGivenDayUseCase,
    private val getScreenTimeDurationForGivenDayUseCase: GetScreenTimeDurationForGivenDayUseCase
) : ViewModel() {

    var state by mutableStateOf(StatisticsScreenState())

    private var allTimeDaysToUnlockEventCounts: List<Pair<Long, Int>> = emptyList()

    fun reload() = viewModelScope.launch {
        allTimeDaysToUnlockEventCounts = getAllTimeDaysToUnlockEventCountsUseCase()

        state = state.copy(
            isInitializing = false,
            allTimeUnlockEventCounts = allTimeDaysToUnlockEventCounts.mapIndexed { index, pair ->
                BarEntry(index.toFloat(), pair.second.toFloat())
            }
        )
    }

    fun onEvent(event: StatisticsScreenEvent) {
         when (event) {
            is StatisticsScreenEvent.ScreenOnEventsInformationDialogVisible -> {
                state = state.copy(isScreenOnEventsInformationDialogVisible = event.isVisible)
            }
            is StatisticsScreenEvent.SelectChartValue -> viewModelScope.launch {
                val chartEntriesSize = state.allTimeUnlockEventCounts.size
                val highlightedDayTimeInMillis = ZonedDateTime.now()
                    .minusDays((chartEntriesSize - event.selectedEntryIndex - 1).toLong())
                    .toTimeInMillis()
                val isScreenTimeLimitEnabled = userSessionRepository.isScreenTimeLimitEnabled()
                val screenTimeLimitDuration = if (isScreenTimeLimitEnabled) {
                    screenTimeLimitsRepository
                        .getScreenTimeLimitActiveAtTime(timeInMillis = highlightedDayTimeInMillis)
                        ?.let {
                            val minuteInMillis = 60000L
                            it.limitAmountMinutes * minuteInMillis
                        }
                } else null

                state = state.copy(
                    currentlyHighlightedDayTimeInMillis = highlightedDayTimeInMillis,
                    unlockEventsCount = getUnlockEventsCountForGivenDayUseCase(
                        dayTimeInMillis = highlightedDayTimeInMillis
                    ),
                    unlockLimitAmount = getUnlockLimitAmountForGivenDayUseCase(
                        dayTimeInMillis = highlightedDayTimeInMillis
                    ),
                    screenOnEventsCount = getScreenOnEventsCountForGivenDayUseCase(
                        dayTimeInMillis = highlightedDayTimeInMillis
                    ),
                    screenTimeDurationMillis = getScreenTimeDurationForGivenDayUseCase(
                        dayTimeInMillis = highlightedDayTimeInMillis
                    ),
                    screenTimeLimitDurationMillis = screenTimeLimitDuration
                )
            }
        }
    }
}