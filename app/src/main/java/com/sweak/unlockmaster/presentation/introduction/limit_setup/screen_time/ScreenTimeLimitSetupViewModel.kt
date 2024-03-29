package com.sweak.unlockmaster.presentation.introduction.limit_setup.screen_time

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweak.unlockmaster.domain.SCREEN_TIME_LIMIT_INTERVAL_MINUTES
import com.sweak.unlockmaster.domain.SCREEN_TIME_LIMIT_MINUTES_LOWER_BOUND
import com.sweak.unlockmaster.domain.SCREEN_TIME_LIMIT_MINUTES_UPPER_BOUND
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import com.sweak.unlockmaster.domain.use_case.screen_time_limits.AddOrUpdateScreenTimeLimitForTodayUseCase
import com.sweak.unlockmaster.domain.use_case.screen_time_limits.GetScreenTimeLimitMinutesForTodayUseCase
import com.sweak.unlockmaster.domain.use_case.screen_time_limits.GetScreenTimeLimitMinutesForTomorrowUseCase
import com.sweak.unlockmaster.presentation.common.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenTimeLimitSetupViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userSessionRepository: UserSessionRepository,
    private val addOrUpdateScreenTimeLimitForTodayUseCase: AddOrUpdateScreenTimeLimitForTodayUseCase,
    private val getScreenTimeLimitMinutesForTodayUseCase: GetScreenTimeLimitMinutesForTodayUseCase,
    private val getScreenTimeLimitMinutesForTomorrowUseCase: GetScreenTimeLimitMinutesForTomorrowUseCase
) : ViewModel() {

    private val isUpdatingExistingScreenTimeLimit: Boolean =
        checkNotNull(savedStateHandle[Screen.KEY_IS_UPDATING_EXISTING_SCREEN_TIME_LIMIT])

    var state by mutableStateOf(ScreenTimeLimitSetupScreenState())

    private val screenTimeLimitSubmittedEventsChannel = Channel<ScreenTimeLimitSubmittedEvent>()
    val screenTimeLimitSubmittedEvents = screenTimeLimitSubmittedEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val screenTimeLimitForToday = getScreenTimeLimitMinutesForTodayUseCase()
            val availableScreenTimeLimitRangeUseCase = IntRange(
                start = SCREEN_TIME_LIMIT_MINUTES_LOWER_BOUND,
                endInclusive = SCREEN_TIME_LIMIT_MINUTES_UPPER_BOUND
            )
            val screenTimeLimitForTomorrow = getScreenTimeLimitMinutesForTomorrowUseCase()

            state = state.copy(
                isScreenTimeLimitEnabled = userSessionRepository.isScreenTimeLimitEnabled(),
                pickedScreenTimeLimitMinutes = screenTimeLimitForToday,
                availableScreenTimeLimitRange = availableScreenTimeLimitRangeUseCase,
                screenTimeLimitIntervalMinutes = SCREEN_TIME_LIMIT_INTERVAL_MINUTES,
                screenTimeLimitMinutesForTomorrow =
                if (screenTimeLimitForTomorrow != null &&
                    screenTimeLimitForToday != screenTimeLimitForTomorrow
                ) screenTimeLimitForTomorrow
                else null
            )
        }
    }

    fun onEvent(event: ScreenTimeLimitSetupScreenEvent) {
        when (event) {
            is ScreenTimeLimitSetupScreenEvent.PickNewScreenTimeLimit -> {
                state = state.copy(
                    pickedScreenTimeLimitMinutes = event.newScreenTimeLimitMinutes,
                    hasUserChangedAnySettings = isUpdatingExistingScreenTimeLimit
                )
            }
            is ScreenTimeLimitSetupScreenEvent.SubmitSelectedScreenTimeLimit -> {
                state.pickedScreenTimeLimitMinutes?.let {
                    viewModelScope.launch {
                        if (isUpdatingExistingScreenTimeLimit) {
                            // TODO
                        } else {
                            addOrUpdateScreenTimeLimitForTodayUseCase(limitAmountMinutes = it)
                        }

                        screenTimeLimitSubmittedEventsChannel.send(ScreenTimeLimitSubmittedEvent)
                    }
                }
            }
            is ScreenTimeLimitSetupScreenEvent.ToggleScreenTimeLimitEnabledState -> {
                viewModelScope.launch {
                    userSessionRepository.setScreenTimeLimitEnabled(isEnabled = event.isEnabled)
                    state = state.copy(isScreenTimeLimitEnabled = event.isEnabled)
                }
            }
            is ScreenTimeLimitSetupScreenEvent.IsRemoveScreenTimeLimitForTomorrowDialogVisible -> {
                state = state.copy(
                    isRemoveScreenTimeLimitForTomorrowDialogVisible = event.isVisible
                )
            }
            is ScreenTimeLimitSetupScreenEvent.ConfirmRemoveScreenTimeLimitForTomorrow -> {
                // TODO
                state = state.copy(
                    screenTimeLimitMinutesForTomorrow = null,
                    isRemoveScreenTimeLimitForTomorrowDialogVisible = false
                )
            }
            is ScreenTimeLimitSetupScreenEvent.IsSettingsNotSavedDialogVisible -> {
                state = state.copy(isSettingsNotSavedDialogVisible =  event.isVisible)
            }
        }
    }

    object ScreenTimeLimitSubmittedEvent
}