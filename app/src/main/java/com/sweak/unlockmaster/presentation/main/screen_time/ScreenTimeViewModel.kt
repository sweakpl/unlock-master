package com.sweak.unlockmaster.presentation.main.screen_time

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.sweak.unlockmaster.domain.model.SessionEvent.CounterPaused
import com.sweak.unlockmaster.domain.model.SessionEvent.ScreenTime
import com.sweak.unlockmaster.domain.use_case.screen_time.GetHourlyUsageMinutesForGivenDayUseCase
import com.sweak.unlockmaster.domain.use_case.screen_time.GetScreenTimeDurationForGivenDayUseCase
import com.sweak.unlockmaster.domain.use_case.screen_time.GetSessionEventsForGivenDayUseCase
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.util.Duration
import com.sweak.unlockmaster.presentation.main.screen_time.ScreenTimeScreenState.UIReadySessionEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenTimeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getHourlyUsageMinutesForGivenDayUseCase: GetHourlyUsageMinutesForGivenDayUseCase,
    private val getScreenTimeDurationForGivenDayUseCase: GetScreenTimeDurationForGivenDayUseCase,
    private val getSessionEventsForGivenDayUseCase: GetSessionEventsForGivenDayUseCase
) : ViewModel() {

    private val displayedDayTimeInMillis: Long =
        checkNotNull(savedStateHandle[Screen.KEY_DISPLAYED_SCREEN_TIME_DAY_MILLIS])

    var state by mutableStateOf(ScreenTimeScreenState())

    fun refresh() = viewModelScope.launch {
        state = state.copy(
            isInitializing = false,
            screenTimeMinutesPerHourEntries =
            getHourlyUsageMinutesForGivenDayUseCase(displayedDayTimeInMillis)
                .mapIndexed { index, minutes -> Entry(index.toFloat(), minutes.toFloat()) },
            todayScreenTimeDuration = Duration(
                durationMillis = getScreenTimeDurationForGivenDayUseCase(displayedDayTimeInMillis),
                precision = Duration.DisplayPrecision.MINUTES
            ),
            UIReadySessionEvents =
            getSessionEventsForGivenDayUseCase(displayedDayTimeInMillis)
                .map {
                    when (it) {
                        is ScreenTime -> {
                            UIReadySessionEvent.ScreenTime(
                                screenSessionStartAndEndTimesInMillis =
                                Pair(it.sessionStartTime, it.sessionEndTime),
                                screenSessionDuration = Duration(it.sessionDuration)
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
}