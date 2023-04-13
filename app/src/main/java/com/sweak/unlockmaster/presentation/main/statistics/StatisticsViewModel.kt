package com.sweak.unlockmaster.presentation.main.statistics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.BarEntry
import com.sweak.unlockmaster.domain.toTimeInMillis
import com.sweak.unlockmaster.domain.use_case.screen_on_events.GetScreenOnEventsCountOfGivenDayUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_events.GetAllTimeDaysToUnlockEventCountsUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_limits.GetUnlockLimitAmountForGivenDayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getAllTimeDaysToUnlockEventCountsUseCase: GetAllTimeDaysToUnlockEventCountsUseCase,
    private val getUnlockLimitAmountForGivenDayUseCase: GetUnlockLimitAmountForGivenDayUseCase,
    private val getScreenOnEventsCountOfGivenDayUseCase: GetScreenOnEventsCountOfGivenDayUseCase
) : ViewModel() {

    var state by mutableStateOf(StatisticsScreenState())

    private var allTimeDaysToUnlockEventCounts: List<Pair<Long, Int>> = emptyList()

    fun reload() = viewModelScope.launch {
        allTimeDaysToUnlockEventCounts = getAllTimeDaysToUnlockEventCountsUseCase()

        state = state.copy(
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
            is StatisticsScreenEvent.ChartValueSelected -> viewModelScope.launch {
                val chartEntriesSize = state.allTimeUnlockEventCounts.size
                val highlightedDayTimeInMillis = ZonedDateTime.now()
                    .minusDays((chartEntriesSize - event.selectedEntryIndex - 1).toLong())
                    .toTimeInMillis()

                state = state.copy(
                    currentlyHighlightedDayTimeInMillis = highlightedDayTimeInMillis,
                    unlockLimitAmount = getUnlockLimitAmountForGivenDayUseCase(
                        dayTimeInMillis = highlightedDayTimeInMillis
                    ),
                    screenOnEventsCount = getScreenOnEventsCountOfGivenDayUseCase(
                        dayTimeInMillis = highlightedDayTimeInMillis
                    )
                )
            }
        }
    }
}