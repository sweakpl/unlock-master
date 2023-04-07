package com.sweak.unlockmaster.presentation.main.statistics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.BarEntry
import com.sweak.unlockmaster.domain.use_case.unlock_events.GetAllTimeDaysToUnlockEventCountsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getAllTimeDaysToUnlockEventCountsUseCase: GetAllTimeDaysToUnlockEventCountsUseCase
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
         }
    }
}