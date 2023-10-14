package com.sweak.unlockmaster.presentation.settings.mobilizing_notifications

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweak.unlockmaster.domain.AVAILABLE_MOBILIZING_NOTIFICATIONS_FREQUENCY_PERCENTAGES
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MobilizingNotificationsViewModel @Inject constructor(
    private val userSessionRepository: UserSessionRepository
) : ViewModel() {

    var state by mutableStateOf(MobilizingNotificationsScreenState())

    private val frequencyPercentageSubmittedEventsChannel =
        Channel<FrequencyPercentageSubmittedEvent>()
    val frequencyPercentageSubmittedEvents =
        frequencyPercentageSubmittedEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val percentage = userSessionRepository.getMobilizingNotificationsFrequencyPercentage()
            val availablePercentages = AVAILABLE_MOBILIZING_NOTIFICATIONS_FREQUENCY_PERCENTAGES
            val selectedPercentageIndex = availablePercentages.indexOf(percentage)

            state = state.copy(
                selectedMobilizingNotificationsFrequencyPercentageIndex = selectedPercentageIndex,
                availableMobilizingNotificationsFrequencyPercentages = availablePercentages
            )
        }
    }

    fun onEvent(event: MobilizingNotificationsScreenEvent) {
        when (event) {
            is MobilizingNotificationsScreenEvent.SelectNewFrequencyPercentageIndex -> {
                state = state.copy(
                    selectedMobilizingNotificationsFrequencyPercentageIndex = event.newPercentageIndex
                )
            }
            is MobilizingNotificationsScreenEvent.ConfirmNewSelectedFrequencyPercentage -> {
                viewModelScope.launch {
                    with(state) {
                        availableMobilizingNotificationsFrequencyPercentages?.let { percentages ->
                            selectedMobilizingNotificationsFrequencyPercentageIndex?.let {
                                userSessionRepository.setMobilizingNotificationsFrequencyPercentage(
                                    percentage = percentages[it]
                                )
                                frequencyPercentageSubmittedEventsChannel.send(
                                    FrequencyPercentageSubmittedEvent
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    object FrequencyPercentageSubmittedEvent
}