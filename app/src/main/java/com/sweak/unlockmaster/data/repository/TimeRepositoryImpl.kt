package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.domain.repository.TimeRepository
import java.time.ZonedDateTime

class TimeRepositoryImpl : TimeRepository {

    override fun getCurrentTimeInMillis(): Long = System.currentTimeMillis()

    override fun getTodayBeginningTimeInMillis(): Long =
        ZonedDateTime.now()
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
            .toInstant()
            .toEpochMilli()
}