package com.sweak.unlockmaster.presentation.daily_wrap_up

sealed class DailyWrapUpScreenEvent {
    data class ScreenOnEventsInformationDialogVisible(val isVisible: Boolean) :
        DailyWrapUpScreenEvent()

    data object ApplySuggestedUnlockLimit : DailyWrapUpScreenEvent()
}
