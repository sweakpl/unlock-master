package com.sweak.unlockmaster.presentation.settings.daily_wrap_ups_setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweak.unlockmaster.domain.MINIMAL_DAILY_WRAP_UPS_NOTIFICATIONS_TIME_HOUR_OF_DAY
import com.sweak.unlockmaster.domain.model.DailyWrapUpsNotificationsTime
import com.sweak.unlockmaster.domain.use_case.daily_wrap_up.GetDailyWrapUpsNotificationsTimeUseCase
import com.sweak.unlockmaster.domain.use_case.daily_wrap_up.ScheduleDailyWrapUpsNotificationsUseCase
import com.sweak.unlockmaster.domain.use_case.daily_wrap_up.SetDailyWrapUpsNotificationsTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyWrapUpsSettingViewModel @Inject constructor(
    private val getDailyWrapUpsNotificationsTimeUseCase: GetDailyWrapUpsNotificationsTimeUseCase,
    private val setDailyWrapUpsNotificationsTimeUseCase: SetDailyWrapUpsNotificationsTimeUseCase,
    private val scheduleDailyWrapUpsNotificationsUseCase: ScheduleDailyWrapUpsNotificationsUseCase
) : ViewModel() {

    var state by mutableStateOf(DailyWrapUpsSettingScreenState())

    private val notificationTimeSubmittedEventsChannel =
        Channel<NotificationTimeSubmittedEvent>()
    val notificationTimeSubmittedEvents =
        notificationTimeSubmittedEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val dailyWrapUpsNotificationsTime = getDailyWrapUpsNotificationsTimeUseCase()

            state = state.copy(
                notificationHourOfDay = dailyWrapUpsNotificationsTime.hourOfDay,
                notificationMinute = dailyWrapUpsNotificationsTime.minute
            )
        }
    }

    fun onEvent(event: DailyWrapUpsSettingScreenEvent) {
        when (event) {
            is DailyWrapUpsSettingScreenEvent.SelectNewDailyWrapUpsSettingNotificationsTime -> {
                state = state.copy(
                    notificationHourOfDay = event.newNotificationHourOfDay,
                    notificationMinute = event.newNotificationMinute,
                )
            }
            is DailyWrapUpsSettingScreenEvent.ConfirmNewSelectedDailyWrapUpsNotificationsTimeSetting -> {
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
                        setDailyWrapUpsNotificationsTimeUseCase(
                            DailyWrapUpsNotificationsTime(
                                notificationHourOfDay,
                                notificationMinute
                            )
                        )
                        scheduleDailyWrapUpsNotificationsUseCase()
                        notificationTimeSubmittedEventsChannel.send(NotificationTimeSubmittedEvent)
                    }
                }
            }
            is DailyWrapUpsSettingScreenEvent.IsInvalidTimeSelectedDialogVisible -> {
                state = state.copy(isInvalidTimeSelectedDialogVisible = event.isVisible)
            }
        }
    }

    object NotificationTimeSubmittedEvent
}