package com.sweak.unlockmaster.presentation.main.statistics

sealed class StatisticsScreenEvent {
    class ScreenOnEventsInformationDialogVisible(val isVisible: Boolean) : StatisticsScreenEvent()
}
