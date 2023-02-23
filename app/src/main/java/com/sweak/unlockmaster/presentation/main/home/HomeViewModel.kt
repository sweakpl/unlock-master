package com.sweak.unlockmaster.presentation.main.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweak.unlockmaster.domain.use_case.counter_pause.IsUnlockCounterPausedUseCase
import com.sweak.unlockmaster.domain.use_case.counter_pause.SetUnlockCounterPauseUseCase
import com.sweak.unlockmaster.domain.use_case.screen_time.GetTodayScreenTimeHoursAndMinutesUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_events.GetTodayUnlockEventsCountUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_limits.GetUnlockLimitAmountForTodayUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_limits.GetUnlockLimitAmountForTomorrowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTodayUnlockEventsCountUseCase: GetTodayUnlockEventsCountUseCase,
    private val getUnlockLimitAmountForTodayUseCase: GetUnlockLimitAmountForTodayUseCase,
    private val getUnlockLimitAmountForTomorrowUseCase: GetUnlockLimitAmountForTomorrowUseCase,
    private val setUnlockCounterPauseUseCase: SetUnlockCounterPauseUseCase,
    private val isUnlockCounterPausedUseCase: IsUnlockCounterPausedUseCase,
    private val getTodayScreenTimeHoursAndMinutesUseCase: GetTodayScreenTimeHoursAndMinutesUseCase
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
            unlockCount = getTodayUnlockEventsCountUseCase(),
            unlockLimit = unlockLimitForToday,
            isUnlockCounterPaused = isUnlockCounterPausedUseCase(),
            unlockLimitForTomorrow =
            if (unlockLimitForTomorrow != unlockLimitForToday) unlockLimitForTomorrow else null,
            todayHoursAndMinutesScreenTimePair = getTodayScreenTimeHoursAndMinutesUseCase()
        )
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.UnlockCounterPauseChanged -> viewModelScope.launch {
                val isUnlockCounterPaused = isUnlockCounterPausedUseCase()

                setUnlockCounterPauseUseCase(isPaused = !isUnlockCounterPaused)
                state = state.copy(isUnlockCounterPaused = !isUnlockCounterPaused)
                event.pauseChangedCallback(!isUnlockCounterPaused)
            }
        }
    }
}