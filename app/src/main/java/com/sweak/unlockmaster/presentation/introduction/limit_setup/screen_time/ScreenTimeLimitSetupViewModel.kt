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
import com.sweak.unlockmaster.domain.use_case.screen_time_limits.AddOrUpdateScreenTimeLimitForTomorrowUseCase
import com.sweak.unlockmaster.domain.use_case.screen_time_limits.DeleteScreenTimeLimitForTomorrowUseCase
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
    private val addOrUpdateScreenTimeLimitForTomorrowUseCase: AddOrUpdateScreenTimeLimitForTomorrowUseCase,
    private val getScreenTimeLimitMinutesForTodayUseCase: GetScreenTimeLimitMinutesForTodayUseCase,
    private val getScreenTimeLimitMinutesForTomorrowUseCase: GetScreenTimeLimitMinutesForTomorrowUseCase,
    private val deleteScreenTimeLimitForTomorrowUseCase: DeleteScreenTimeLimitForTomorrowUseCase
) : ViewModel() {

    private val isUpdatingExistingScreenTimeLimit: Boolean =
        checkNotNull(savedStateHandle[Screen.KEY_IS_UPDATING_EXISTING_SCREEN_TIME_LIMIT])

    var state by mutableStateOf(ScreenTimeLimitSetupScreenState())

    private val selectedSettingsConfirmedEventsChannel = Channel<SelectedSettingsConfirmedEvent>()
    val screenTimeLimitSubmittedEvents = selectedSettingsConfirmedEventsChannel.receiveAsFlow()

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
                pickedScreenTimeLimitMinutes =
                screenTimeLimitForTomorrow ?: screenTimeLimitForToday,
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
            is ScreenTimeLimitSetupScreenEvent.ConfirmSelectedSettings -> {
                viewModelScope.launch {
                    with (state) {
                        if (pickedScreenTimeLimitMinutes != null &&
                            isScreenTimeLimitEnabled != null
                        ) {
                            if (isUpdatingExistingScreenTimeLimit) {
                                addOrUpdateScreenTimeLimitForTomorrowUseCase(
                                    limitAmountMinutes = pickedScreenTimeLimitMinutes
                                )
                            } else {
                                addOrUpdateScreenTimeLimitForTodayUseCase(
                                    limitAmountMinutes = pickedScreenTimeLimitMinutes
                                )
                            }

                            val currentIsScreenTimeLimitEnabled =
                                userSessionRepository.isScreenTimeLimitEnabled()

                            // We're checking if the screen time limit state actually changed to
                            // prevent redundant screenTimeLimitStateChangedCallback calls:
                            if (currentIsScreenTimeLimitEnabled != isScreenTimeLimitEnabled) {
                                userSessionRepository.setScreenTimeLimitEnabled(
                                    isEnabled = isScreenTimeLimitEnabled
                                )
                                event.screenTimeLimitStateChangedCallback(isScreenTimeLimitEnabled)
                            }

                            selectedSettingsConfirmedEventsChannel.send(
                                SelectedSettingsConfirmedEvent
                            )
                        }
                    }
                }
            }
            is ScreenTimeLimitSetupScreenEvent.TryToggleScreenTimeLimitState -> {
                state.isScreenTimeLimitEnabled?.let {
                    state = if (it) {
                        state.copy(isScreenTimeLimitDisableConfirmationDialogVisible = true)
                    } else {
                        state.copy(
                            isScreenTimeLimitEnabled = true,
                            hasUserChangedAnySettings = isUpdatingExistingScreenTimeLimit
                        )
                    }
                }
            }
            is ScreenTimeLimitSetupScreenEvent.DisableScreenTimeLimit -> {
                state = state.copy(
                    isScreenTimeLimitEnabled = false,
                    hasUserChangedAnySettings = isUpdatingExistingScreenTimeLimit,
                    isScreenTimeLimitDisableConfirmationDialogVisible = false
                )
            }
            is ScreenTimeLimitSetupScreenEvent.IsScreenTimeLimitDisableConfirmationDialogVisible -> {
                state = state.copy(
                    isScreenTimeLimitDisableConfirmationDialogVisible = event.isVisible
                )
            }
            is ScreenTimeLimitSetupScreenEvent.IsRemoveScreenTimeLimitForTomorrowDialogVisible -> {
                state = state.copy(
                    isRemoveScreenTimeLimitForTomorrowDialogVisible = event.isVisible
                )
            }
            is ScreenTimeLimitSetupScreenEvent.ConfirmRemoveScreenTimeLimitForTomorrow -> {
                viewModelScope.launch {
                    deleteScreenTimeLimitForTomorrowUseCase()
                    state = state.copy(
                        screenTimeLimitMinutesForTomorrow = null,
                        isRemoveScreenTimeLimitForTomorrowDialogVisible = false
                    )
                }
            }
            is ScreenTimeLimitSetupScreenEvent.IsSettingsNotSavedDialogVisible -> {
                state = state.copy(isSettingsNotSavedDialogVisible =  event.isVisible)
            }
        }
    }

    object SelectedSettingsConfirmedEvent
}