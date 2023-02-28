package com.sweak.unlockmaster.domain.repository

interface TimeRepository {
    fun getCurrentTimeInMillis(): Long
    fun getTodayBeginningTimeInMillis(): Long
    fun getTomorrowBeginningTimeInMillis(): Long
    fun getSixDaysBeforeDayBeginningTimeInMillis(): Long
    fun getBeginningOfGivenDayTimeInMillis(timeInMillis: Long): Long
}