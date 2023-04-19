package com.sweak.unlockmaster.domain.use_case.screen_on_events

import com.sweak.unlockmaster.domain.repository.ScreenOnEventsRepository
import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.toTimeInMillis
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

class GetScreenOnEventsCountForGivenDayUseCase @Inject constructor(
    private val screenOnEventsRepository: ScreenOnEventsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke(dayTimeInMillis: Long): Int {
        val beginningOfGivenDay = ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(
                timeRepository.getBeginningOfGivenDayTimeInMillis(dayTimeInMillis)
            ),
            ZoneId.systemDefault()
        )
        val endingOfGivenDay = beginningOfGivenDay.plusDays(1)

        return screenOnEventsRepository.getScreenOnEventsSinceTimeAndUntilTime(
            sinceTimeInMillis = beginningOfGivenDay.toTimeInMillis(),
            untilTimeInMillis = endingOfGivenDay.toTimeInMillis()
        ).size
    }
}