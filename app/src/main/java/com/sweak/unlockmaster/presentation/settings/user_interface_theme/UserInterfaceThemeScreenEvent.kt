package com.sweak.unlockmaster.presentation.settings.user_interface_theme

import com.sweak.unlockmaster.domain.model.UiThemeMode

sealed class UserInterfaceThemeScreenEvent {
    data class SelectUiThemeMode(val uiThemeMode: UiThemeMode) : UserInterfaceThemeScreenEvent()
}