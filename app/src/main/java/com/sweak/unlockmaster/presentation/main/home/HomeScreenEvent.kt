package com.sweak.unlockmaster.presentation.main.home

sealed class HomeScreenEvent {
    data class UnlockCounterPauseChanged(
        val pauseChangedCallback: (Boolean) -> Unit
    ) : HomeScreenEvent()
}
