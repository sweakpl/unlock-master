package com.sweak.unlockmaster.domain.use_case.screen_time

import com.sweak.unlockmaster.domain.model.UnlockMasterEvent
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.CounterPausedEvent
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.CounterUnpausedEvent
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.LockEvent
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.UnlockEvent
import com.sweak.unlockmaster.domain.repository.CounterPausedEventsRepository
import com.sweak.unlockmaster.domain.repository.CounterUnpausedEventsRepository
import com.sweak.unlockmaster.domain.repository.LockEventsRepository
import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.repository.UnlockEventsRepository
import javax.inject.Inject

class GetScreenTimeDurationForGivenDayUseCase @Inject constructor(
    private val unlockEventsRepository: UnlockEventsRepository,
    private val lockEventsRepository: LockEventsRepository,
    private val counterPausedEventsRepository: CounterPausedEventsRepository,
    private val counterUnpausedEventsRepository: CounterUnpausedEventsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke(
        dayTimeInMillis: Long = timeRepository.getCurrentTimeInMillis()
    ): Long {
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

        val consideredScreenEvents = allScreenEvents
            .filter {
                it.timeInMillis in givenDayBeginningTimeInMillis until nextDayBeginningTimeInMillis
            }
            .run {
                val tempAllScreenEvents = this.toMutableList()

                initiallyConsideredScreenEvent?.let {
                    tempAllScreenEvents.add(initiallyConsideredScreenEvent)
                }

                return@run tempAllScreenEvents
            }
            .sortedBy { it.timeInMillis }

        val isCurrentDayTheGivenDay =
            timeRepository.getBeginningOfGivenDayTimeInMillis(currentTimeInMillis) ==
                    givenDayBeginningTimeInMillis

        if (consideredScreenEvents.isEmpty()) {
            return if (!isCurrentDayTheGivenDay) 0L
            else currentTimeInMillis - givenDayBeginningTimeInMillis
        }

        var screenTimeDuration = 0L
        var previousUnlockMasterEvent: UnlockMasterEvent = consideredScreenEvents.first()

        if (previousUnlockMasterEvent.run { this is LockEvent || this is CounterPausedEvent }) {
            screenTimeDuration += previousUnlockMasterEvent.timeInMillis - givenDayBeginningTimeInMillis
        }

        var previousSinceTime: Long =
            if (previousUnlockMasterEvent.run { this is UnlockEvent || this is CounterUnpausedEvent }) {
                previousUnlockMasterEvent.timeInMillis
            } else {
                givenDayBeginningTimeInMillis
            }

        consideredScreenEvents.subList(1, consideredScreenEvents.size).forEach {
            if (it is UnlockEvent || it is CounterUnpausedEvent) {
                previousSinceTime = it.timeInMillis
            } else if (it is LockEvent || it is CounterPausedEvent) {
                if (previousUnlockMasterEvent.run { this is UnlockEvent || this is CounterUnpausedEvent }) {
                    screenTimeDuration += it.timeInMillis - previousSinceTime
                }
            }

            previousUnlockMasterEvent = it
        }

        if (previousUnlockMasterEvent.run { this is UnlockEvent || this is CounterUnpausedEvent }) {
            screenTimeDuration +=
                (if (isCurrentDayTheGivenDay) currentTimeInMillis
                else nextDayBeginningTimeInMillis) -
                        previousSinceTime
        }

        return screenTimeDuration
    }
}