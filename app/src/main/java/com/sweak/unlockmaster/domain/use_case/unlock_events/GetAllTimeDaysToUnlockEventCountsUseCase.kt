package com.sweak.unlockmaster.domain.use_case.unlock_events

import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.repository.UnlockEventsRepository
import com.sweak.unlockmaster.domain.toTimeInMillis
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

class GetAllTimeDaysToUnlockEventCountsUseCase @Inject constructor(
    private val unlockEventsRepository: UnlockEventsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke(): List<Pair<Long, Int>> {
        val firstUnlockEvent = unlockEventsRepository.getFirstUnlockEvent()
        val allTimeUnlockEvents = unlockEventsRepository.getUnlockEventsSinceTime(
            sinceTimeInMillis = firstUnlockEvent?.timeInMillis ?: 0
        )

        val dateToUnlockCountsMap = allTimeUnlockEvents.groupingBy {
            timeRepository.getBeginningOfGivenDayTimeInMillis(it.timeInMillis)
        }.eachCount()

        val allTimeDaysToUnlockEventCountsPairs = mutableListOf<Pair<Long, Int>>()

        var daysCounter = 0
        var currentCountingDayDate = ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(timeRepository.getTomorrowBeginningTimeInMillis()),
            ZoneId.systemDefault()
        ).minusDays(1)
        val firstUnlockEventDayDate =
            if (firstUnlockEvent != null) {
                ZonedDateTime.ofInstant(
                    Instant.ofEpochMilli(
                        timeRepository.getBeginningOfGivenDayTimeInMillis(
                            firstUnlockEvent.timeInMillis
                        )
                    ),
                    ZoneId.systemDefault()
                )
            } else {
                currentCountingDayDate
            }

        while (daysCounter < 7 || currentCountingDayDate >= firstUnlockEventDayDate) {
            allTimeDaysToUnlockEventCountsPairs.add(
                Pair(
                    currentCountingDayDate.toTimeInMillis(),
                    dateToUnlockCountsMap.getOrDefault(
                        currentCountingDayDate.toTimeInMillis(),
                        0
                    )
                )
            )

            daysCounter += 1
            currentCountingDayDate = currentCountingDayDate.minusDays(1)
        }

        return allTimeDaysToUnlockEventCountsPairs.reversed()
    }
}