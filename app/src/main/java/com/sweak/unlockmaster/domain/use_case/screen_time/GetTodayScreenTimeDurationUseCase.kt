package com.sweak.unlockmaster.domain.use_case.screen_time

import com.sweak.unlockmaster.domain.model.*
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.*
import com.sweak.unlockmaster.domain.repository.*
import javax.inject.Inject

class GetTodayScreenTimeDurationUseCase @Inject constructor(
    private val unlockEventsRepository: UnlockEventsRepository,
    private val lockEventsRepository: LockEventsRepository,
    private val counterPausedEventsRepository: CounterPausedEventsRepository,
    private val counterUnpausedEventsRepository: CounterUnpausedEventsRepository,
    private val timeRepository: TimeRepository,
    private val userSessionRepository: UserSessionRepository
) {
    suspend operator fun invoke(): Long {
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
            return if (userSessionRepository.isUnlockCounterPaused()) 0L
            else currentTimeInMillis - todayBeginningTimeInMillis
        }

        var screenTimeDuration = 0L
        var previousUnlockMasterEvent: UnlockMasterEvent = screenEvents.first()

        if (previousUnlockMasterEvent.run { this is LockEvent || this is CounterPausedEvent }) {
            screenTimeDuration += previousUnlockMasterEvent.timeInMillis - todayBeginningTimeInMillis
        }

        var previousSinceTime: Long =
            if (previousUnlockMasterEvent.run { this is UnlockEvent || this is CounterUnpausedEvent }) {
                previousUnlockMasterEvent.timeInMillis
            } else {
                todayBeginningTimeInMillis
            }

        screenEvents.subList(1, screenEvents.size).forEach {
            if (it is UnlockEvent || it is CounterUnpausedEvent) {
                previousSinceTime = it.timeInMillis
            } else if (it.run { this is LockEvent || this is CounterPausedEvent }) {
                if (previousUnlockMasterEvent.run { this is UnlockEvent || this is CounterUnpausedEvent }) {
                    screenTimeDuration += it.timeInMillis - previousSinceTime
                }
            }

            previousUnlockMasterEvent = it
        }

        if (previousUnlockMasterEvent.run { this is UnlockEvent || this is CounterUnpausedEvent }) {
            screenTimeDuration += currentTimeInMillis - previousSinceTime
        }

        return screenTimeDuration
    }
}