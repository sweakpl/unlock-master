package com.sweak.unlockmaster.domain.use_case.screen_time

import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.*
import com.sweak.unlockmaster.domain.repository.CounterPausedEventsRepository
import com.sweak.unlockmaster.domain.repository.CounterUnpausedEventsRepository
import com.sweak.unlockmaster.domain.repository.LockEventsRepository
import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.repository.UnlockEventsRepository
import com.sweak.unlockmaster.domain.toTimeInMillis
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

class GetHourlyUsageMinutesForGivenDayUseCase @Inject constructor(
    private val unlockEventsRepository: UnlockEventsRepository,
    private val lockEventsRepository: LockEventsRepository,
    private val counterPausedEventsRepository: CounterPausedEventsRepository,
    private val counterUnpausedEventsRepository: CounterUnpausedEventsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke(
        dayTimeInMillis: Long = timeRepository.getCurrentTimeInMillis()
    ): List<Int> {
        val givenDayBeginningTimeInMillis = timeRepository
            .getBeginningOfGivenDayTimeInMillis(dayTimeInMillis)
        val previousDayBeginningTimeInMillis = givenDayBeginningTimeInMillis - 86400000
        val nextDayBeginningTimeInMillis = givenDayBeginningTimeInMillis + 86400000
        val currentTimeInMillis = timeRepository.getCurrentTimeInMillis()

        val unlockEvents = unlockEventsRepository
            .getUnlockEventsSinceTime(sinceTimeInMillis = previousDayBeginningTimeInMillis)
        val lockEvents = lockEventsRepository
            .getLockEventsSinceTime(sinceTimeInMillis = previousDayBeginningTimeInMillis)
        val counterUnpausedEvents = counterUnpausedEventsRepository
            .getCounterUnpausedEventsSinceTime(sinceTimeInMillis = previousDayBeginningTimeInMillis)
        val counterPausedEvents = counterPausedEventsRepository
            .getCounterPausedEventsSinceTime(sinceTimeInMillis = previousDayBeginningTimeInMillis)

        val allScreenEvents =
            (unlockEvents + lockEvents + counterUnpausedEvents + counterPausedEvents)

        val latestPreviousDayScreenEvent = allScreenEvents
            .filter { it.timeInMillis < givenDayBeginningTimeInMillis }
            .maxByOrNull { it.timeInMillis }

        val initiallyConsideredScreenEvent = when (latestPreviousDayScreenEvent) {
            is UnlockEvent -> UnlockEvent(givenDayBeginningTimeInMillis)
            is LockEvent -> LockEvent(givenDayBeginningTimeInMillis)
            is CounterPausedEvent -> CounterPausedEvent(givenDayBeginningTimeInMillis)
            is CounterUnpausedEvent -> CounterUnpausedEvent(givenDayBeginningTimeInMillis)
            else -> null
        }

        val isCurrentDayTheGivenDay =
            timeRepository.getBeginningOfGivenDayTimeInMillis(currentTimeInMillis) ==
                    givenDayBeginningTimeInMillis

        val actualScreenEventsFromGivenDay = allScreenEvents.filter {
            it.timeInMillis in givenDayBeginningTimeInMillis until nextDayBeginningTimeInMillis
        }

        if (!isCurrentDayTheGivenDay &&
            actualScreenEventsFromGivenDay.isEmpty() &&
            initiallyConsideredScreenEvent !is UnlockEvent &&
            initiallyConsideredScreenEvent !is CounterUnpausedEvent
        ) {
            return listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        }

        val consideredScreenEvents = actualScreenEventsFromGivenDay.run {
            val tempAllScreenEvents = this.toMutableList()

            initiallyConsideredScreenEvent?.let {
                tempAllScreenEvents.add(initiallyConsideredScreenEvent)
            }

            tempAllScreenEvents.add(
                LockEvent(
                    if (isCurrentDayTheGivenDay) currentTimeInMillis
                    else nextDayBeginningTimeInMillis
                )
            )

            return@run tempAllScreenEvents
        }
            .sortedBy { it.timeInMillis }

        var hourIntervalStart = ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(givenDayBeginningTimeInMillis),
            ZoneId.systemDefault()
        )
        var hourIntervalEnd = hourIntervalStart.plusHours(1)

        var shouldAddToCurrentHourlyDuration =
            consideredScreenEvents.first().run { this is LockEvent || this is CounterPausedEvent }
        var currentHourlyDuration = 0L
        val hourlyDurationsInMillis = mutableListOf<Long>()

        repeat(24) {
            val currentIntervalScreenEvents = consideredScreenEvents
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