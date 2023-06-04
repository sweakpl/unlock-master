package com.sweak.unlockmaster.domain.use_case.screen_time

import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.*
import com.sweak.unlockmaster.domain.repository.*
import com.sweak.unlockmaster.domain.toTimeInMillis
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

class GetTodayHourlyUsageMinutesUseCase @Inject constructor(
    private val unlockEventsRepository: UnlockEventsRepository,
    private val lockEventsRepository: LockEventsRepository,
    private val counterPausedEventsRepository: CounterPausedEventsRepository,
    private val counterUnpausedEventsRepository: CounterUnpausedEventsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke(): List<Int> {
        val todayBeginningTimeInMillis = timeRepository.getTodayBeginningTimeInMillis()
        val currentTimeInMillis = timeRepository.getCurrentTimeInMillis()

        val unlockEvents = unlockEventsRepository
            .getUnlockEventsSinceTime(sinceTimeInMillis = todayBeginningTimeInMillis)
        val lockEvents = lockEventsRepository
            .getLockEventsSinceTime(sinceTimeInMillis = todayBeginningTimeInMillis)
        val counterUnpausedEvents = counterUnpausedEventsRepository
            .getCounterUnpausedEventsSinceTime(sinceTimeInMillis = todayBeginningTimeInMillis)
        val counterPausedEvents = counterPausedEventsRepository
            .getCounterPausedEventsSinceTime(sinceTimeInMillis = todayBeginningTimeInMillis)

        val screenEvents = (
                unlockEvents + lockEvents + counterUnpausedEvents + counterPausedEvents +
                        LockEvent(currentTimeInMillis) // current time can be treated as lock event
                )

        var hourIntervalStart = ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(todayBeginningTimeInMillis),
            ZoneId.systemDefault()
        )
        var hourIntervalEnd = hourIntervalStart.plusHours(1)

        var shouldAddToCurrentHourlyDuration =
            screenEvents.first().run { this is LockEvent || this is CounterPausedEvent }
        var currentHourlyDuration = 0L
        val hourlyDurationsInMillis = mutableListOf<Long>()

        repeat(24) {
            val currentIntervalScreenEvents = screenEvents
                .filter {
                    it.timeInMillis >= hourIntervalStart.toTimeInMillis() &&
                            it.timeInMillis <= hourIntervalEnd.toTimeInMillis()
                }.sortedBy {
                    it.timeInMillis
                }

            var hasAlreadyAddedDuration = false

            if (currentIntervalScreenEvents.isEmpty() && shouldAddToCurrentHourlyDuration) {
                hourlyDurationsInMillis.add(3600000) // 60 minutes
                hasAlreadyAddedDuration = true
            }

            var durationCountingStartTimeInMillis = hourIntervalStart.toTimeInMillis()

            currentIntervalScreenEvents.forEach {
                if (it.run { this is LockEvent || this is CounterPausedEvent } &&
                    shouldAddToCurrentHourlyDuration
                ) {
                    currentHourlyDuration += it.timeInMillis - durationCountingStartTimeInMillis
                    shouldAddToCurrentHourlyDuration = false
                } else if (it.run { this is UnlockEvent || this is CounterUnpausedEvent }) {
                    durationCountingStartTimeInMillis = it.timeInMillis
                    shouldAddToCurrentHourlyDuration = true
                }
            }

            if (shouldAddToCurrentHourlyDuration) {
                currentHourlyDuration +=
                    hourIntervalEnd.toTimeInMillis() - durationCountingStartTimeInMillis
            }

            if (!hasAlreadyAddedDuration) {
                hourlyDurationsInMillis.add(currentHourlyDuration)
            }

            hourIntervalStart = hourIntervalStart.plusHours(1)
            hourIntervalEnd = hourIntervalEnd.plusHours(1)
            currentHourlyDuration = 0L
        }

        return hourlyDurationsInMillis.map { (it / 60000).toInt() }
    }
}