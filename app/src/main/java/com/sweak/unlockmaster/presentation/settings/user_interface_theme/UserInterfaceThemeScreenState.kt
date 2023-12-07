package com.sweak.unlockmaster.presentation.settings.user_interface_theme

import com.sweak.unlockmaster.domain.model.UiThemeMode

data class UserInterfaceThemeScreenState(
    val selectedUiThemeMode: UiThemeMode = UiThemeMode.SYSTEM,
    val availableUiThemeModes: List<UiThemeMode> = UiThemeMode.entries.toList()
)
