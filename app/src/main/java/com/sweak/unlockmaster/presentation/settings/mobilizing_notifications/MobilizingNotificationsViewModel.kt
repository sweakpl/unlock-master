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

    private val selectedSettingsConfirmedEventsChannel = Channel<SelectedSettingsConfirmedEvent>()
    val frequencyPercentageSubmittedEvents = selectedSettingsConfirmedEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val percentage = userSessionRepository.getMobilizingNotificationsFrequencyPercentage()
            val availablePercentages = AVAILABLE_MOBILIZING_NOTIFICATIONS_FREQUENCY_PERCENTAGES
            val selectedPercentageIndex = availablePercentages.indexOf(percentage)
            val areOverLimitNotificationsEnabled =
                userSessionRepository.areOverUnlockLimitMobilizingNotificationsEnabled()

            state = state.copy(
                selectedMobilizingNotificationsFrequencyPercentageIndex = selectedPercentageIndex,
                availableMobilizingNotificationsFrequencyPercentages = availablePercentages,
                areOverLimitNotificationsEnabled = areOverLimitNotificationsEnabled
            )
        }
    }

    fun onEvent(event: MobilizingNotificationsScreenEvent) {
        when (event) {
            is MobilizingNotificationsScreenEvent.SelectNewFrequencyPercentageIndex -> {
                state = state.copy(
                    selectedMobilizingNotificationsFrequencyPercentageIndex =
                    event.newPercentageIndex,
                    hasUserChangedAnySettings = true
                )
            }
            is MobilizingNotificationsScreenEvent.ToggleOverLimitNotifications -> {
                state = state.copy(
                    areOverLimitNotificationsEnabled = event.areOverLimitNotificationsEnabled,
                    hasUserChangedAnySettings = true
                )
            }
            is MobilizingNotificationsScreenEvent.ConfirmSelectedSettings -> {
                viewModelScope.launch {
                    with(state) {
                        if (availableMobilizingNotificationsFrequencyPercentages != null &&
                            selectedMobilizingNotificationsFrequencyPercentageIndex != null &&
                            areOverLimitNotificationsEnabled != null
                        ) {
                            userSessionRepository.setMobilizingNotificationsFrequencyPercentage(
                                percentage = availableMobilizingNotificationsFrequencyPercentages[
                                    selectedMobilizingNotificationsFrequencyPercentageIndex
                                ]
                            )
                            userSessionRepository
                                .setOverUnlockLimitMobilizingNotificationsEnabled(
                                    areEnabled = areOverLimitNotificationsEnabled
                                )
                            selectedSettingsConfirmedEventsChannel.send(
                                SelectedSettingsConfirmedEvent
                            )
                        }
                    }
                }
            }
            is MobilizingNotificationsScreenEvent.IsSettingsNotSavedDialogVisible -> {
                state = state.copy(isSettingsNotSavedDialogVisible = event.isVisible)
            }
        }
    }

    object SelectedSettingsConfirmedEvent
}