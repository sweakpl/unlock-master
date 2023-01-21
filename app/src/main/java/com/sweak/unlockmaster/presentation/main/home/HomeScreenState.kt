package com.sweak.unlockmaster.presentation.main.home

data class HomeScreenState(
    val isInitializing: Boolean = true,
    val unlockCount: Int? = null,
    val unlockLimit: Int? = null
)
