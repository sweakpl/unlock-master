package com.sweak.unlockmaster.presentation.settings.mobilizing_notifications

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweak.unlockmaster.domain.use_case.mobilizing_notifications.GetAvailableMobilizingNotificationsFrequencyPercentages
import com.sweak.unlockmaster.domain.use_case.mobilizing_notifications.GetMobilizingNotificationsFrequencyPercentage
import com.sweak.unlockmaster.domain.use_case.mobilizing_notifications.SetMobilizingNotificationsFrequencyPercentage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MobilizingNotificationsViewModel @Inject constructor(
    private val getMobilizingNotificationsFrequencyPercentage: GetMobilizingNotificationsFrequencyPercentage,
    private val setMobilizingNotificationsFrequencyPercentage: SetMobilizingNotificationsFrequencyPercentage,
    private val getAvailableMobilizingNotificationsFrequencyPercentages: GetAvailableMobilizingNotificationsFrequencyPercentages
) : ViewModel() {

    var state by mutableStateOf(MobilizingNotificationsScreenState())

    private val frequencyPercentageSubmittedEventsChannel =
        Channel<FrequencyPercentageSubmittedEvent>()
    val frequencyPercentageSubmittedEvents =
        frequencyPercentageSubmittedEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val percentage = getMobilizingNotificationsFrequencyPercentage()
            val availablePercentages = getAvailableMobilizingNotificationsFrequencyPercentages()
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
                                setMobilizingNotificationsFrequencyPercentage(
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