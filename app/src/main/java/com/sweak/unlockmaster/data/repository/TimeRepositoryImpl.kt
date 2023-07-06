package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.toTimeInMillis
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class TimeRepositoryImpl : TimeRepository {

    override fun getCurrentTimeInMillis(): Long = System.currentTimeMillis()

    override fun getTodayBeginningTimeInMillis(): Long =
        ZonedDateTime.now()
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
            .toTimeInMillis()

    override fun getTomorrowBeginningTimeInMillis(): Long =
        ZonedDateTime.now()
            .plusDays(1)
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
            .toTimeInMillis()

    override fun getSixDaysBeforeDayBeginningTimeInMillis(): Long =
        ZonedDateTime.now()
            .minusDays(6)
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
            .toTimeInMillis()

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

    override fun getFutureTimeInMillisOfSpecifiedHourOfDayAndMinute(
        hourOfDay: Int,
        minute: Int
    ): Long {
        var alarmZonedDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault())
        alarmZonedDateTime = alarmZonedDateTime.withHour(hourOfDay)
        alarmZonedDateTime = alarmZonedDateTime.withMinute(minute)
        alarmZonedDateTime = alarmZonedDateTime.withSecond(0)
        alarmZonedDateTime = alarmZonedDateTime.withNano(0)

        if (alarmZonedDateTime.toInstant().toEpochMilli() <= getCurrentTimeInMillis()) {
            alarmZonedDateTime = alarmZonedDateTime.plusDays(1)
        }

        return alarmZonedDateTime.toInstant().toEpochMilli()
    }
}