package com.sweak.unlockmaster.domain.use_case.unlock_events

import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.repository.UnlockEventsRepository
import com.sweak.unlockmaster.presentation.common.util.toTimeInMillis
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

        var intervalBeginningCursorDateTime = ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(sixDaysBeforeDayBeginningTimeInMillis),
            ZoneId.systemDefault()
        )
        var intervalEndingCursorDateTime = intervalBeginningCursorDateTime.plusDays(1)

        var currentDayUnlockCount = 0
        var currentDayIndex = 0
        val lastWeekUnlockEventCountsList = mutableListOf(0, 0, 0, 0, 0, 0, 0)
        val tomorrowBeginningTimeInMillis = timeRepository.getTomorrowBeginningTimeInMillis()

        lastWeekUnlockEvents.forEach {
            while (it.timeInMillis < intervalBeginningCursorDateTime.toTimeInMillis() ||
                it.timeInMillis >= intervalEndingCursorDateTime.toTimeInMillis()
            ) {
                lastWeekUnlockEventCountsList[currentDayIndex] = currentDayUnlockCount
                currentDayUnlockCount = 0
                currentDayIndex++

                intervalBeginningCursorDateTime = intervalBeginningCursorDateTime.plusDays(1)
                intervalEndingCursorDateTime = intervalEndingCursorDateTime.plusDays(1)

                if (intervalBeginningCursorDateTime.toTimeInMillis() == tomorrowBeginningTimeInMillis) {
                    break
                }
            }

            currentDayUnlockCount++
        }

        lastWeekUnlockEventCountsList[currentDayIndex] = currentDayUnlockCount

        return lastWeekUnlockEventCountsList
    }
}