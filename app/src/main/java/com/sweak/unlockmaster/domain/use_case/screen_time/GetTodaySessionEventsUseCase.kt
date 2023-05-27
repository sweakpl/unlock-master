package com.sweak.unlockmaster.domain.use_case.screen_time

import com.sweak.unlockmaster.domain.model.SessionEvent
import com.sweak.unlockmaster.domain.model.SessionEvent.CounterPaused
import com.sweak.unlockmaster.domain.model.SessionEvent.ScreenTime
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.*
import com.sweak.unlockmaster.domain.repository.*
import javax.inject.Inject

class GetTodaySessionEventsUseCase @Inject constructor(
    private val unlockEventsRepository: UnlockEventsRepository,
    private val lockEventsRepository: LockEventsRepository,
    private val counterPausedEventsRepository: CounterPausedEventsRepository,
    private val counterUnpausedEventsRepository: CounterUnpausedEventsRepository,
    private val timeRepository: TimeRepository,
    private val userSessionRepository: UserSessionRepository
) {
    suspend operator fun invoke(): List<SessionEvent> {
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

        val screenEvents = (unlockEvents + lockEvents + counterUnpausedEvents + counterPausedEvents)
            .sortedBy { it.timeInMillis }

        if (screenEvents.isEmpty()) {
            // TODO: In the test for the varying day: additional check for isGivenDayCurrentDay
            return if (userSessionRepository.isUnlockCounterPaused()) {
                listOf(CounterPaused(todayBeginningTimeInMillis, currentTimeInMillis))
            } else {
                listOf(
                    ScreenTime(
                        todayBeginningTimeInMillis,
                        currentTimeInMillis,
                        currentTimeInMillis - todayBeginningTimeInMillis
                    )
                )
            }
        }

        val allSessionEvents = mutableListOf<SessionEvent>()
        var previousUnlockMasterEvent: UnlockMasterEvent = screenEvents.first()
        var previousSinceTime: Long =
            if (previousUnlockMasterEvent.run { this is UnlockEvent || this is CounterUnpausedEvent }) {
                previousUnlockMasterEvent.timeInMillis
            } else {
                todayBeginningTimeInMillis
            }

        if (previousUnlockMasterEvent is LockEvent || previousUnlockMasterEvent is CounterPausedEvent) {
            allSessionEvents.add(
                ScreenTime(
                    todayBeginningTimeInMillis,
                    previousUnlockMasterEvent.timeInMillis,
                    previousUnlockMasterEvent.timeInMillis - todayBeginningTimeInMillis
                )
            )
        } else if (previousUnlockMasterEvent is CounterUnpausedEvent) {
            allSessionEvents.add(
                CounterPaused(todayBeginningTimeInMillis, previousUnlockMasterEvent.timeInMillis)
            )
        }

        screenEvents.subList(1, screenEvents.size).forEach {
            if (previousUnlockMasterEvent is UnlockEvent && (it is LockEvent || it is CounterPausedEvent)) {
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
            } else if (previousUnlockMasterEvent is CounterPausedEvent && it is CounterUnpausedEvent) {
                allSessionEvents.add(CounterPaused(previousSinceTime, it.timeInMillis))
            }

            if (it is UnlockEvent || it is CounterPausedEvent) {
                previousSinceTime = it.timeInMillis
            }

            previousUnlockMasterEvent = it
        }

        previousSinceTime = previousUnlockMasterEvent.timeInMillis

        if (previousUnlockMasterEvent is UnlockEvent || previousUnlockMasterEvent is CounterUnpausedEvent) {
            allSessionEvents.add(
                ScreenTime(
                    previousSinceTime,
                    currentTimeInMillis,
                    currentTimeInMillis - previousSinceTime
                )
            )
        } else if (previousUnlockMasterEvent is CounterPausedEvent) {
            allSessionEvents.add(CounterPaused(previousSinceTime, currentTimeInMillis))
        }

        return allSessionEvents
    }
}