package com.sweak.unlockmaster.domain.use_case.unlock_events

import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.repository.UnlockEventsRepository
import com.sweak.unlockmaster.domain.toTimeInMillis
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

class GetLastWeekUnlockEventCountsUseCase @Inject constructor(
    private val unlockEventsRepository: UnlockEventsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke(): List<Int> {
        val sixDaysBeforeDayBeginningTimeInMillis =
            timeRepository.getSixDaysBeforeDayBeginningTimeInMillis()
        val lastWeekUnlockEvents = unlockEventsRepository.getUnlockEventsSinceTime(
            sinceTimeInMillis = sixDaysBeforeDayBeginningTimeInMillis
        )

        val dateToUnlockCountsMap = lastWeekUnlockEvents.groupingBy {
            timeRepository.getBeginningOfGivenDayTimeInMillis(it.timeInMillis)
        }.eachCount()

        val lastWeekUnlockEventCountsList = mutableListOf<Int>()
        var sixDaysBeforeDayBeginningDate = ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(sixDaysBeforeDayBeginningTimeInMillis),
            ZoneId.systemDefault()
        )

        repeat(7) {
            lastWeekUnlockEventCountsList.add(
                dateToUnlockCountsMap.getOrDefault(
                    sixDaysBeforeDayBeginningDate.toTimeInMillis(),
                    0
                )
            )
            sixDaysBeforeDayBeginningDate = sixDaysBeforeDayBeginningDate.plusDays(1)
        }

        return lastWeekUnlockEventCountsList
    }
}