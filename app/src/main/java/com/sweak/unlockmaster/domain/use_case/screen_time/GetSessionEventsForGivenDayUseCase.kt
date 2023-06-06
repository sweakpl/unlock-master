package com.sweak.unlockmaster.domain.use_case.screen_time

import com.sweak.unlockmaster.domain.model.SessionEvent
import com.sweak.unlockmaster.domain.model.SessionEvent.*
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.*
import com.sweak.unlockmaster.domain.repository.CounterPausedEventsRepository
import com.sweak.unlockmaster.domain.repository.CounterUnpausedEventsRepository
import com.sweak.unlockmaster.domain.repository.LockEventsRepository
import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.repository.UnlockEventsRepository
import javax.inject.Inject

class GetSessionEventsForGivenDayUseCase @Inject constructor(
    private val unlockEventsRepository: UnlockEventsRepository,
    private val lockEventsRepository: LockEventsRepository,
    private val counterPausedEventsRepository: CounterPausedEventsRepository,
    private val counterUnpausedEventsRepository: CounterUnpausedEventsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke(
        dayTimeInMillis: Long = timeRepository.getCurrentTimeInMillis()
    ): List<SessionEvent> {
        val givenDayBeginningTimeInMillis = timeRepository
            .getBeginningOfGivenDayTimeInMillis(dayTimeInMillis)
        val previousDayBeginningTimeInMillis = givenDayBeginningTimeInMillis - 86400000
        val nextDayBeginningTimeInMillis = givenDayBeginningTimeInMillis + 86400000
        val currentTimeInMillis = timeRepository.getCurrentTimeInMillis()

        val unlockEvents = unlockEventsRepository
            .getUnlockEventsSinceTime(previousDayBeginningTimeInMillis)
        val lockEvents = lockEventsRepository
            .getLockEventsSinceTime(previousDayBeginningTimeInMillis)
        val counterUnpausedEvents = counterUnpausedEventsRepository
            .getCounterUnpausedEventsSinceTime(previousDayBeginningTimeInMillis)
        val counterPausedEvents = counterPausedEventsRepository
            .getCounterPausedEventsSinceTime(previousDayBeginningTimeInMillis)

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

        if (actualScreenEventsFromGivenDay.isEmpty() &&
            initiallyConsideredScreenEvent !is UnlockEvent &&
            initiallyConsideredScreenEvent !is CounterUnpausedEvent
        ) {
            if (!isCurrentDayTheGivenDay) {
                return if (initiallyConsideredScreenEvent is CounterPausedEvent) {
                    listOf(
                        CounterPaused(givenDayBeginningTimeInMillis, nextDayBeginningTimeInMillis)
                    )
                } else {
                    emptyList()
                }
            } else if (initiallyConsideredScreenEvent == null){
                return listOf(
                    ScreenTime(
                        givenDayBeginningTimeInMillis,
                        currentTimeInMillis,
                        currentTimeInMillis - givenDayBeginningTimeInMillis
                    )
                )
            }
        }

        val consideredScreenEvents = actualScreenEventsFromGivenDay.run {
            val tempAllScreenEvents = this.toMutableList()

            initiallyConsideredScreenEvent?.let {
                tempAllScreenEvents.add(initiallyConsideredScreenEvent)
            }

            return@run tempAllScreenEvents
        }
            .sortedBy { it.timeInMillis }

        val allSessionEvents = mutableListOf<SessionEvent>()
        var previousUnlockMasterEvent: UnlockMasterEvent = consideredScreenEvents.first()
        var previousSinceTime: Long =
            if (previousUnlockMasterEvent is UnlockEvent ||
                previousUnlockMasterEvent is CounterUnpausedEvent
            ) {
                previousUnlockMasterEvent.timeInMillis
            } else {
                givenDayBeginningTimeInMillis
            }

        if (previousUnlockMasterEvent is LockEvent ||
            previousUnlockMasterEvent is CounterPausedEvent
        ) {
            val sessionDuration = previousUnlockMasterEvent.timeInMillis - givenDayBeginningTimeInMillis

            if (sessionDuration != 0L) {
                allSessionEvents.add(
                    ScreenTime(
                        givenDayBeginningTimeInMillis,
                        previousUnlockMasterEvent.timeInMillis,
                        sessionDuration
                    )
                )
            }
        } else if (previousUnlockMasterEvent is CounterUnpausedEvent) {
            if (previousUnlockMasterEvent.timeInMillis - givenDayBeginningTimeInMillis != 0L) {
                allSessionEvents.add(
                    CounterPaused(
                        givenDayBeginningTimeInMillis,
                        previousUnlockMasterEvent.timeInMillis
                    )
                )
            }
        }

        consideredScreenEvents.subList(1, consideredScreenEvents.size).forEach {
            if (previousUnlockMasterEvent is UnlockEvent &&
                (it is LockEvent || it is CounterPausedEvent)
            ) {
                allSessionEvents.add(
                    ScreenTime(
                        previousSinceTime,
                        it.timeInMillis,
                        it.timeInMillis - previousSinceTime
                    )
                )
            } else if (previousUnlockMasterEvent is CounterUnpausedEvent && it is LockEvent) {
                allSessionEvents.add(
                    ScreenTime(
                        previousUnlockMasterEvent.timeInMillis,
                        it.timeInMillis,
                        it.timeInMillis - previousUnlockMasterEvent.timeInMillis
                    )
                )
            } else if (previousUnlockMasterEvent is CounterPausedEvent &&
                it is CounterUnpausedEvent
            ) {
                allSessionEvents.add(CounterPaused(previousSinceTime, it.timeInMillis))
            }

            if (it is UnlockEvent || it is CounterPausedEvent) {
                previousSinceTime = it.timeInMillis
            }

            previousUnlockMasterEvent = it
        }

        previousSinceTime = previousUnlockMasterEvent.timeInMillis

        if (previousUnlockMasterEvent is UnlockEvent ||
            previousUnlockMasterEvent is CounterUnpausedEvent
        ) {
            val untilTime =
                if (isCurrentDayTheGivenDay) currentTimeInMillis else nextDayBeginningTimeInMillis

            allSessionEvents.add(
                ScreenTime(
                    previousSinceTime,
                    untilTime,
                    untilTime - previousSinceTime
                )
            )
        } else if (previousUnlockMasterEvent is CounterPausedEvent) {
            allSessionEvents.add(
                CounterPaused(
                    previousSinceTime,
                    if (isCurrentDayTheGivenDay) currentTimeInMillis
                    else nextDayBeginningTimeInMillis
                )
            )
        }

        return allSessionEvents
    }
}