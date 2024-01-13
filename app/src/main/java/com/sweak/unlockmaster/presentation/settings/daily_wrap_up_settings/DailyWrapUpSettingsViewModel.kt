package com.sweak.unlockmaster.presentation.settings.daily_wrap_up_settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweak.unlockmaster.domain.MINIMAL_DAILY_WRAP_UPS_NOTIFICATIONS_TIME_HOUR_OF_DAY
import com.sweak.unlockmaster.domain.model.DailyWrapUpNotificationsTime
import com.sweak.unlockmaster.domain.use_case.daily_wrap_up.GetDailyWrapUpNotificationsTimeUseCase
import com.sweak.unlockmaster.domain.use_case.daily_wrap_up.ScheduleDailyWrapUpNotificationsUseCase
import com.sweak.unlockmaster.domain.use_case.daily_wrap_up.SetDailyWrapUpNotificationsTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyWrapUpSettingsViewModel @Inject constructor(
    private val getDailyWrapUpNotificationsTimeUseCase: GetDailyWrapUpNotificationsTimeUseCase,
    private val setDailyWrapUpNotificationsTimeUseCase: SetDailyWrapUpNotificationsTimeUseCase,
    private val scheduleDailyWrapUpNotificationsUseCase: ScheduleDailyWrapUpNotificationsUseCase
) : ViewModel() {

    var state by mutableStateOf(DailyWrapUpSettingsScreenState())

    private val notificationTimeSubmittedEventsChannel =
        Channel<NotificationTimeSubmittedEvent>()
    val notificationTimeSubmittedEvents =
        notificationTimeSubmittedEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val dailyWrapUpNotificationsTime = getDailyWrapUpNotificationsTimeUseCase()

            state = state.copy(
                notificationHourOfDay = dailyWrapUpNotificationsTime.hourOfDay,
                notificationMinute = dailyWrapUpNotificationsTime.minute
            )
        }
    }

    fun onEvent(event: DailyWrapUpSettingsScreenEvent) {
        when (event) {
            is DailyWrapUpSettingsScreenEvent.SelectNewDailyWrapUpSettingsNotificationsTime -> {
                state = state.copy(
                    notificationHourOfDay = event.newNotificationHourOfDay,
                    notificationMinute = event.newNotificationMinute,
                    hasInitialTimeBeenSet = true,
                    hasUserChangedAnySettings = state.hasInitialTimeBeenSet
                )
            }
            is DailyWrapUpSettingsScreenEvent.ConfirmNewSelectedDailyWrapUpSettings -> {
                val notificationHourOfDay = state.notificationHourOfDay

                if (notificationHourOfDay != null &&
                    notificationHourOfDay < MINIMAL_DAILY_WRAP_UPS_NOTIFICATIONS_TIME_HOUR_OF_DAY
                ) {
                    state = state.copy(isInvalidTimeSelectedDialogVisible = true)
                    return
                }

                viewModelScope.launch {
                    val notificationMinute = state.notificationMinute

                    if (notificationHourOfDay != null && notificationMinute != null) {
                        setDailyWrapUpNotificationsTimeUseCase(
                            DailyWrapUpNotificationsTime(notificationHourOfDay, notificationMinute)
                        )
                        scheduleDailyWrapUpNotificationsUseCase()
                        notificationTimeSubmittedEventsChannel.send(NotificationTimeSubmittedEvent)
                    }
                }
            }
            is DailyWrapUpSettingsScreenEvent.IsInvalidTimeSelectedDialogVisible -> {
                state = state.copy(isInvalidTimeSelectedDialogVisible = event.isVisible)
            }
            is DailyWrapUpSettingsScreenEvent.IsSettingsNotSavedDialogVisible -> {
                state = state.copy(isSettingsNotSavedDialogVisible = event.isVisible)
            }
        }
    }

    object NotificationTimeSubmittedEvent
}