package com.sweak.unlockmaster.presentation.main.screen_time

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.sweak.unlockmaster.domain.use_case.screen_time.GetTodayHourlyUsageMinutesUseCase
import com.sweak.unlockmaster.domain.use_case.screen_time.GetTodayScreenTimeHoursAndMinutesUseCase
import com.sweak.unlockmaster.presentation.main.screen_time.ScreenTimeScreenState.SessionEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenTimeViewModel @Inject constructor(
    private val getTodayHourlyUsageMinutesUseCase: GetTodayHourlyUsageMinutesUseCase,
    private val getTodayScreenTimeHoursAndMinutesUseCase: GetTodayScreenTimeHoursAndMinutesUseCase
) : ViewModel() {

    var state by mutableStateOf(ScreenTimeScreenState())

    fun refresh() = viewModelScope.launch {
        state = state.copy(
            isInitializing = false,
            screenTimeMinutesPerHourEntries = getTodayHourlyUsageMinutesUseCase()
                .mapIndexed { index, minutes -> Entry(index.toFloat(), minutes.toFloat()) },
            todayHoursAndMinutesScreenTimePair = getTodayScreenTimeHoursAndMinutesUseCase(),
            sessionEvents = listOf(
                SessionEvent.ScreenTimeSessionEvent(
                    Pair(1678345200000, 1678345260000),
                    Triple(0, 0, 15)
                ),
                SessionEvent.ScreenTimeSessionEvent(
                    Pair(1678350960000, 1678351260000),
                    Triple(0, 13, 2)
                ),
                SessionEvent.CounterPausedSessionEvent(
                    Pair(1678351260000, 1678354320000)
                ),
                SessionEvent.ScreenTimeSessionEvent(
                    Pair(1678354320000, 1678354620000),
                    Triple(0, 5, 0)
                ),
                SessionEvent.ScreenTimeSessionEvent(
                    Pair(1678356720000, 1678360380000),
                    Triple(1, 1, 12)
                )
            )
        )
    }
}