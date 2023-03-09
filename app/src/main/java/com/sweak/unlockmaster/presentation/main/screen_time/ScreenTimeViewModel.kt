package com.sweak.unlockmaster.presentation.main.screen_time

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.sweak.unlockmaster.domain.use_case.screen_time.GetTodayScreenTimeHoursAndMinutesUseCase
import com.sweak.unlockmaster.presentation.main.screen_time.ScreenTimeScreenState.SessionEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenTimeViewModel @Inject constructor(
    private val getTodayScreenTimeHoursAndMinutesUseCase: GetTodayScreenTimeHoursAndMinutesUseCase
) : ViewModel() {

    var state by mutableStateOf(ScreenTimeScreenState())

    fun refresh() = viewModelScope.launch {
        state = state.copy(
            isInitializing = false,
            screenTimeMinutesPerHourEntries = listOf(
                Entry(0f, 0f),
                Entry(1f, 0f),
                Entry(2f, 0f),
                Entry(3f, 4f),
                Entry(4f, 12f),
                Entry(5f, 0f),
                Entry(6f, 0f),
                Entry(7f, 0f),
                Entry(8f, 23f),
                Entry(9f, 45f),
                Entry(10f, 3f),
                Entry(11f, 0f),
                Entry(12f, 0f),
                Entry(13f, 28f),
                Entry(14f, 12f),
                Entry(15f, 2f),
                Entry(16f, 4f),
                Entry(17f, 0f),
                Entry(18f, 0f),
                Entry(19f, 56f),
                Entry(20f, 34f),
                Entry(21f, 4f),
                Entry(22f, 0f),
                Entry(23f, 0f)
            ),
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