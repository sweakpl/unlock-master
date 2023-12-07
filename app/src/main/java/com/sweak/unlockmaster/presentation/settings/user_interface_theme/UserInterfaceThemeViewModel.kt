package com.sweak.unlockmaster.presentation.settings.user_interface_theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInterfaceThemeViewModel @Inject constructor(
    private val userSessionRepository: UserSessionRepository
) : ViewModel() {

    var state by mutableStateOf(UserInterfaceThemeScreenState())

    init {
        viewModelScope.launch {
            userSessionRepository.getUiThemeModeFlow().collect {
                state = state.copy(selectedUiThemeMode = it)
            }
        }
    }

    fun onEvent(event: UserInterfaceThemeScreenEvent) {
        when (event) {
            is UserInterfaceThemeScreenEvent.SelectUiThemeMode -> {
                if (event.uiThemeMode == state.selectedUiThemeMode) return

                viewModelScope.launch {
                    userSessionRepository.setUiThemeMode(event.uiThemeMode)
                    state = state.copy(selectedUiThemeMode = event.uiThemeMode)
                }
            }
        }
    }
}