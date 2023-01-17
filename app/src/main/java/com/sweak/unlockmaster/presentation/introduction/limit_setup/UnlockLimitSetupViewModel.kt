package com.sweak.unlockmaster.presentation.introduction.limit_setup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweak.unlockmaster.domain.DEFAULT_UNLOCK_LIMIT
import com.sweak.unlockmaster.domain.use_case.unlock_limits.AddOrUpdateUnlockLimitForTodayUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_limits.GetUnlockLimitForTodayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnlockLimitSetupViewModel @Inject constructor(
    private val addOrUpdateUnlockLimitForTodayUseCase: AddOrUpdateUnlockLimitForTodayUseCase,
    private val getUnlockLimitForTodayUseCase: GetUnlockLimitForTodayUseCase
) : ViewModel() {

    var pickedUnlockLimit by mutableStateOf<Int?>(null)

    private val unlockLimitSubmittedEventsChannel = Channel<UnlockLimitSubmittedEvent>()
    val unlockEventsSubmittedEvents = unlockLimitSubmittedEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            pickedUnlockLimit = getUnlockLimitForTodayUseCase() ?: DEFAULT_UNLOCK_LIMIT
        }
    }

    fun pickNewUnlockLimit(limitAmount: Int) {
        pickedUnlockLimit = limitAmount
    }

    fun submitUnlockLimitForToday() {
        viewModelScope.launch {
            pickedUnlockLimit?.let {
                addOrUpdateUnlockLimitForTodayUseCase(limitAmount = it)
                unlockLimitSubmittedEventsChannel.send(UnlockLimitSubmittedEvent)
            }
        }
    }

    object UnlockLimitSubmittedEvent
}