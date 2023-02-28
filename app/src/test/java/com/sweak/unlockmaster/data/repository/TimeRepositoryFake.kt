package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.toTimeInMillis
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class TimeRepositoryFake : TimeRepository {

    var currentTimeInMillisToBeReturned: Long = 0
    var todayBeginningTimeInMillisToBeReturned: Long = 0
    var tomorrowBeginningTimeInMillisToBeReturned: Long = 0
    var sixDaysBeforeDayBeginningTimeInMillisToBeReturned: Long = 0

    override fun getCurrentTimeInMillis(): Long = currentTimeInMillisToBeReturned

    override fun getTodayBeginningTimeInMillis(): Long = todayBeginningTimeInMillisToBeReturned

    override fun getTomorrowBeginningTimeInMillis(): Long =
        tomorrowBeginningTimeInMillisToBeReturned

    override fun getSixDaysBeforeDayBeginningTimeInMillis(): Long =
        sixDaysBeforeDayBeginningTimeInMillisToBeReturned

    override fun getBeginningOfGivenDayTimeInMillis(timeInMillis: Long): Long =
        ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(timeInMillis),
            ZoneId.systemDefault()
        )
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
            .toTimeInMillis()
}