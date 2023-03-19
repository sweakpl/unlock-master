package com.sweak.unlockmaster.presentation.main.screen_time

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.sweak.unlockmaster.domain.model.SessionEvent.*
import com.sweak.unlockmaster.domain.use_case.screen_time.GetTodayHourlyUsageMinutesUseCase
import com.sweak.unlockmaster.domain.use_case.screen_time.GetTodayScreenTimeHoursAndMinutesUseCase
import com.sweak.unlockmaster.domain.use_case.screen_time.GetTodaySessionEventsUseCase
import com.sweak.unlockmaster.presentation.main.screen_time.ScreenTimeScreenState.UIReadySessionEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenTimeViewModel @Inject constructor(
    private val getTodayHourlyUsageMinutesUseCase: GetTodayHourlyUsageMinutesUseCase,
    private val getTodayScreenTimeHoursAndMinutesUseCase: GetTodayScreenTimeHoursAndMinutesUseCase,
    private val getTodaySessionEventsUseCase: GetTodaySessionEventsUseCase
) : ViewModel() {

    var state by mutableStateOf(ScreenTimeScreenState())

    fun refresh() = viewModelScope.launch {
        state = state.copy(
            isInitializing = false,
            screenTimeMinutesPerHourEntries = getTodayHourlyUsageMinutesUseCase()
                .mapIndexed { index, minutes -> Entry(index.toFloat(), minutes.toFloat()) },
            todayHoursAndMinutesScreenTimePair = getTodayScreenTimeHoursAndMinutesUseCase(),
            UIReadySessionEvents = getTodaySessionEventsUseCase().map {
                when (it) {
                    is ScreenTime -> {
                        UIReadySessionEvent.ScreenTime(
                            screenSessionStartAndEndTimesInMillis =
                            Pair(it.sessionStartTime, it.sessionEndTime),
                            screenSessionHoursMinutesAndSecondsDurationTriple =
                            getHoursMinutesAndSecondsDurationTriple(it.sessionDuration)
                        )
                    }
                    is CounterPaused -> {
                        UIReadySessionEvent.CounterPaused(
                            counterPauseSessionStartAndEndTimesInMillis =
                            Pair(it.sessionStartTime, it.sessionEndTime)
                        )
                    }
                }
            }
        )
    }

    private fun getHoursMinutesAndSecondsDurationTriple(durationTimeInMillis: Long):
            Triple<Int, Int, Int> {
        val hours = durationTimeInMillis / 3600000
        val minutes = (durationTimeInMillis % 3600000) / 60000
        val seconds = (durationTimeInMillis % 60000) / 1000

        return Triple(hours.toInt(), minutes.toInt(), seconds.toInt())
    }
}