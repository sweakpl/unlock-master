package com.sweak.unlockmaster.presentation.main.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.BarEntry
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import com.sweak.unlockmaster.domain.use_case.screen_time.GetScreenTimeDurationForGivenDayUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_events.GetLastWeekUnlockEventCountsUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_events.GetUnlockEventsCountForGivenDayUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_limits.GetUnlockLimitAmountForTodayUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_limits.GetUnlockLimitAmountForTomorrowUseCase
import com.sweak.unlockmaster.presentation.common.util.Duration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUnlockEventsCountForGivenDayUseCase: GetUnlockEventsCountForGivenDayUseCase,
    private val getUnlockLimitAmountForTodayUseCase: GetUnlockLimitAmountForTodayUseCase,
    private val getUnlockLimitAmountForTomorrowUseCase: GetUnlockLimitAmountForTomorrowUseCase,
    private val getScreenTimeDurationForGivenDayUseCase: GetScreenTimeDurationForGivenDayUseCase,
    private val getLastWeekUnlockEventCountsUseCase: GetLastWeekUnlockEventCountsUseCase,
    private val userSessionRepository: UserSessionRepository
) : ViewModel() {

    var state by mutableStateOf(HomeScreenState())

    fun refresh() = viewModelScope.launch {
        // This half a second delay should give the UnlockMasterService enough time to update all
        // values (in case of e.g. unlocking the screen and immediately landing on the HomeScreen)
        // before we post them to the UI:
        delay(500)

        val unlockLimitForToday = getUnlockLimitAmountForTodayUseCase()
        val unlockLimitForTomorrow = getUnlockLimitAmountForTomorrowUseCase()

        state = state.copy(
            isInitializing = false,
            unlockCount = getUnlockEventsCountForGivenDayUseCase(),
            unlockLimit = unlockLimitForToday,
            isUnlockCounterPaused = userSessionRepository.isUnlockCounterPaused(),
            shouldShowUnlockMasterBlockedWarning =
            userSessionRepository.shouldShowUnlockMasterBlockedWarning(),
            unlockLimitForTomorrow =
            if (unlockLimitForTomorrow != unlockLimitForToday) unlockLimitForTomorrow else null,
            todayScreenTimeDuration = Duration(
                durationMillis = getScreenTimeDurationForGivenDayUseCase(),
                precision = Duration.DisplayPrecision.MINUTES
            ),
            lastWeekUnlockEventCounts = getLastWeekUnlockEventCountsUseCase().mapIndexed { x, y ->
                BarEntry(x.toFloat(), y.toFloat())
            }
        )
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.TryPauseOrUnpauseUnlockCounter -> viewModelScope.launch {
                val isUnlockCounterPaused = userSessionRepository.isUnlockCounterPaused()

                if (isUnlockCounterPaused) {
                    userSessionRepository.setUnlockCounterPaused(isPaused = false)
                    state = state.copy(
                        isUnlockCounterPaused = false,
                        isUnlockCounterPauseConfirmationDialogVisible = false
                    )
                    event.pauseChangedCallback(false)
                } else {
                    state = state.copy(isUnlockCounterPauseConfirmationDialogVisible = true)
                }
            }
            is HomeScreenEvent.PauseUnlockCounter -> viewModelScope.launch {
                userSessionRepository.setUnlockCounterPaused(isPaused = true)
                state = state.copy(
                    isUnlockCounterPaused = true,
                    isUnlockCounterPauseConfirmationDialogVisible = false
                )
                event.pauseChangedCallback(true)
            }
            is HomeScreenEvent.IsUnlockCounterPauseConfirmationDialogVisible -> {
                state = state.copy(isUnlockCounterPauseConfirmationDialogVisible = event.isVisible)
            }
            is HomeScreenEvent.DismissUnlockMasterBlockedWarning -> viewModelScope.launch {
                userSessionRepository.setShouldShowUnlockMasterBlockedWarning(false)
                state = state.copy(shouldShowUnlockMasterBlockedWarning = false)
            }
        }
    }
}