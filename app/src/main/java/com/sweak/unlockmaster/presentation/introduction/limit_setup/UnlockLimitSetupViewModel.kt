package com.sweak.unlockmaster.presentation.introduction.limit_setup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweak.unlockmaster.domain.use_case.unlock_limits.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnlockLimitSetupViewModel @Inject constructor(
    private val addOrUpdateUnlockLimitForTodayUseCase: AddOrUpdateUnlockLimitForTodayUseCase,
    private val addOrUpdateUnlockLimitForTomorrowUseCase: AddOrUpdateUnlockLimitForTomorrowUseCase,
    private val getUnlockLimitAmountForTodayUseCase: GetUnlockLimitAmountForTodayUseCase,
    private val getUnlockLimitAmountForTomorrowUseCase: GetUnlockLimitAmountForTomorrowUseCase,
    private val getAvailableUnlockLimitRangeUseCase: GetAvailableUnlockLimitRangeUseCase,
    private val deleteUnlockLimitForTomorrowUseCase: DeleteUnlockLimitForTomorrowUseCase
) : ViewModel() {

    var state by mutableStateOf(UnlockLimitSetupScreenState())

    private val unlockLimitSubmittedEventsChannel = Channel<UnlockLimitSubmittedEvent>()
    val unlockLimitSubmittedEvents = unlockLimitSubmittedEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val unlockLimitForToday = getUnlockLimitAmountForTodayUseCase()
            val availableUnlockLimitRangeUseCase = getAvailableUnlockLimitRangeUseCase()
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
                state = state.copy(pickedUnlockLimit = event.newUnlockLimit)
            }
            is UnlockLimitSetupScreenEvent.SubmitSelectedUnlockLimit -> {
                state.pickedUnlockLimit?.let {
                    viewModelScope.launch {
                        if (event.isUpdating) {
                            addOrUpdateUnlockLimitForTomorrowUseCase(limitAmount = it)
                            unlockLimitSubmittedEventsChannel.send(UnlockLimitSubmittedEvent)
                        } else {
                            addOrUpdateUnlockLimitForTodayUseCase(limitAmount = it)
                            unlockLimitSubmittedEventsChannel.send(UnlockLimitSubmittedEvent)
                        }
                    }
                }
            }
            is UnlockLimitSetupScreenEvent.RemoveUnlockLimitForTomorrowDialogVisibilityChanged -> {
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
        }
    }

    object UnlockLimitSubmittedEvent
}