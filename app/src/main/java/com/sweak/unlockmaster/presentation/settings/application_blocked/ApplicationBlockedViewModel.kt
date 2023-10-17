package com.sweak.unlockmaster.presentation.settings.application_blocked

import android.os.Build
import android.os.PowerManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ApplicationBlockedViewModel @Inject constructor(
    private val powerManager: PowerManager,
    @Named("PackageName") private val packageName: String
) : ViewModel() {

    var state by mutableStateOf(ApplicationBlockedScreenState())

    init {
        viewModelScope.launch {
            state = state.copy(
                isIgnoringBatteryOptimizations =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    powerManager.isIgnoringBatteryOptimizations(packageName)
                } else true
            )
        }
    }

    fun onEvent(event: ApplicationBlockedScreenEvent) {
        state = when (event) {
            is ApplicationBlockedScreenEvent.CheckIfIgnoringBatteryOptimizations ->
                state.copy(
                    isIgnoringBatteryOptimizations =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        powerManager.isIgnoringBatteryOptimizations(packageName)
                    } else true
                )

            is ApplicationBlockedScreenEvent.IsIgnoreBatteryOptimizationsRequestUnavailableDialogVisible ->
                state.copy(
                    isIgnoreBatteryOptimizationsRequestUnavailable = true,
                    isIgnoreBatteryOptimizationsRequestUnavailableDialogVisible = event.isVisible
                )
        }
    }
}