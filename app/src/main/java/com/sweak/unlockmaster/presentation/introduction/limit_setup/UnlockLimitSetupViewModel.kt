package com.sweak.unlockmaster.presentation.introduction.limit_setup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweak.unlockmaster.domain.UNLOCK_LIMIT_LOWER_BOUND
import com.sweak.unlockmaster.domain.UNLOCK_LIMIT_UPPER_BOUND
import com.sweak.unlockmaster.domain.use_case.unlock_limits.*
import com.sweak.unlockmaster.presentation.common.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnlockLimitSetupViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val addOrUpdateUnlockLimitForTodayUseCase: AddOrUpdateUnlockLimitForTodayUseCase,
    private val addOrUpdateUnlockLimitForTomorrowUseCase: AddOrUpdateUnlockLimitForTomorrowUseCase,
    private val getUnlockLimitAmountForTodayUseCase: GetUnlockLimitAmountForTodayUseCase,
    private val getUnlockLimitAmountForTomorrowUseCase: GetUnlockLimitAmountForTomorrowUseCase,
    private val deleteUnlockLimitForTomorrowUseCase: DeleteUnlockLimitForTomorrowUseCase
) : ViewModel() {

    private val isUpdatingExistingUnlockLimit: Boolean =
        checkNotNull(savedStateHandle[Screen.KEY_IS_UPDATING_EXISTING_UNLOCK_LIMIT])

    var state by mutableStateOf(UnlockLimitSetupScreenState())

    private val unlockLimitSubmittedEventsChannel = Channel<UnlockLimitSubmittedEvent>()
    val unlockLimitSubmittedEvents = unlockLimitSubmittedEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val unlockLimitForToday = getUnlockLimitAmountForTodayUseCase()
            val availableUnlockLimitRangeUseCase = IntRange(
                start = UNLOCK_LIMIT_LOWER_BOUND,
                endInclusive = UNLOCK_LIMIT_UPPER_BOUND
            )
            val unlockLimitForTomorrow = getUnlockLimitAmountForTomorrowUseCase()

            state = state.copy(
                pickedUnlockLimit = unlockLimitForTomorrow ?: unlockLimitForToday,
                availableUnlockLimitRange = availableUnlockLimitRangeUseCase,
                unlockLimitForTomorrow =
                if (unlockLimitForTomorrow != null && unlockLimitForToday != unlockLimitForTomorrow)
                    unlockLimitForTomorrow
                else null
            )
        }
    }

    fun onEvent(event: UnlockLimitSetupScreenEvent) {
        when (event) {
            is UnlockLimitSetupScreenEvent.PickNewUnlockLimit -> {
                state = state.copy(
                    pickedUnlockLimit = event.newUnlockLimit,
                    hasUserChangedAnySettings = isUpdatingExistingUnlockLimit
                )
            }
            is UnlockLimitSetupScreenEvent.SubmitSelectedUnlockLimit -> {
                state.pickedUnlockLimit?.let {
                    viewModelScope.launch {
                        if (isUpdatingExistingUnlockLimit) {
                            addOrUpdateUnlockLimitForTomorrowUseCase(limitAmount = it)
                            unlockLimitSubmittedEventsChannel.send(UnlockLimitSubmittedEvent)
                        } else {
                            addOrUpdateUnlockLimitForTodayUseCase(limitAmount = it)
                            unlockLimitSubmittedEventsChannel.send(UnlockLimitSubmittedEvent)
                        }
                    }
                }
            }
            is UnlockLimitSetupScreenEvent.IsRemoveUnlockLimitForTomorrowDialogVisible -> {
                state = state.copy(isRemoveUnlockLimitForTomorrowDialogVisible = event.isVisible)
            }
            is UnlockLimitSetupScreenEvent.ConfirmRemoveUnlockLimitForTomorrow -> {
                viewModelScope.launch {
                    deleteUnlockLimitForTomorrowUseCase()
                    state = state.copy(
                        unlockLimitForTomorrow = null,
                        isRemoveUnlockLimitForTomorrowDialogVisible = false
                    )
                }
            }
            is UnlockLimitSetupScreenEvent.IsSettingsNotSavedDialogVisible -> {
                state = state.copy(isSettingsNotSavedDialogVisible =  event.isVisible)
            }
        }
    }

    object UnlockLimitSubmittedEvent
}