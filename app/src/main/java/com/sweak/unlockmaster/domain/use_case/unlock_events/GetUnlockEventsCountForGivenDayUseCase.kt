package com.sweak.unlockmaster.domain.use_case.unlock_events

import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.repository.UnlockEventsRepository
import com.sweak.unlockmaster.domain.toTimeInMillis
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

class GetUnlockEventsCountForGivenDayUseCase @Inject constructor(
    private val unlockEventsRepository: UnlockEventsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke(dayTimeInMillis: Long): Int {
        val dayBeginningDateTime = ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(timeRepository.getBeginningOfGivenDayTimeInMillis(dayTimeInMillis)),
            ZoneId.systemDefault()
        )
        val dayEndingDateTime = dayBeginningDateTime.plusDays(1)

        return unlockEventsRepository.getUnlockEventsSinceTimeAndUntilTime(
            sinceTimeInMillis = dayBeginningDateTime.toTimeInMillis(),
            untilTimeInMillis = dayEndingDateTime.toTimeInMillis()
        ).size
    }
}