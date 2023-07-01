package com.sweak.unlockmaster.presentation.main.statistics

sealed class StatisticsScreenEvent {
    class SelectChartValue(val selectedEntryIndex: Int) : StatisticsScreenEvent()
    class ScreenOnEventsInformationDialogVisible(val isVisible: Boolean) : StatisticsScreenEvent()
}
