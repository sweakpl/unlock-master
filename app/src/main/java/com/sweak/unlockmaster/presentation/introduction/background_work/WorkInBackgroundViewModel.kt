package com.sweak.unlockmaster.presentation.introduction.background_work

import android.os.Build
import android.os.PowerManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class WorkInBackgroundViewModel @Inject constructor(
    private val powerManager: PowerManager,
    @Named("PackageName") private val packageName: String
) : ViewModel() {

    var state by mutableStateOf(WorkInBackgroundScreenState())

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

    fun onEvent(event: WorkInBackgroundScreenEvent) {
        when (event) {
            is WorkInBackgroundScreenEvent.CheckIfIgnoringBatteryOptimizations ->
                viewModelScope.launch {
                    // This delay is supposed to ensure that
                    // powerManager.isIgnoringBatteryOptimizations returns an updated value - it can
                    // take some time until it is updated on some systems.
                    delay(1000)

                    state = state.copy(
                        isIgnoringBatteryOptimizations =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            powerManager.isIgnoringBatteryOptimizations(packageName)
                        } else true
                    )
                }
            is WorkInBackgroundScreenEvent.IsIgnoreBatteryOptimizationsRequestUnavailableDialogVisible ->
                state = state.copy(
                    isIgnoreBatteryOptimizationsRequestUnavailable = true,
                    isIgnoreBatteryOptimizationsRequestUnavailableDialogVisible = event.isVisible
                )
            is WorkInBackgroundScreenEvent.UserTriedToGrantNotificationsPermission ->
                state = state.copy(hasUserTriedToGrantNotificationsPermission = true)
            is WorkInBackgroundScreenEvent.IsNotificationsPermissionDialogVisible ->
                state = state.copy(isNotificationsPermissionDialogVisible = event.isVisible)
            is WorkInBackgroundScreenEvent.IsWebBrowserNotFoundDialogVisible ->
                state = state.copy(isWebBrowserNotFoundDialogVisible = event.isVisible)
        }
    }
}