package com.sweak.unlockmaster.domain.use_case.screen_time

import com.sweak.unlockmaster.domain.model.LockEvent
import com.sweak.unlockmaster.domain.model.ScreenEvent
import com.sweak.unlockmaster.domain.model.UnlockEvent
import com.sweak.unlockmaster.domain.repository.*
import javax.inject.Inject

class GetTodayScreenTimeHoursAndMinutesUseCase @Inject constructor(
    private val unlockEventsRepository: UnlockEventsRepository,
    private val lockEventsRepository: LockEventsRepository,
    private val counterPausedEventsRepository: CounterPausedEventsRepository,
    private val counterUnpausedEventsRepository: CounterUnpausedEventsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke(): Pair<Int, Int> {
        val todayBeginningTimeInMillis = timeRepository.getTodayBeginningTimeInMillis()
        val currentTimeInMillis = timeRepository.getCurrentTimeInMillis()

        val unlockEvents = unlockEventsRepository.getUnlockEventsSinceTime(
            sinceTimeInMillis = todayBeginningTimeInMillis
        )
        val lockEvents = lockEventsRepository.getLockEventsSinceTime(
            sinceTimeInMillis = todayBeginningTimeInMillis
        )

        // Counter pauses and unpauses can be treated as fake locks and unlocks since pausing also
        // should affect the screen time like normal unlock and lock events.
        val fakeUnlockEvents = counterUnpausedEventsRepository.getCounterUnpausedEventsSinceTime(
            sinceTimeInMillis = todayBeginningTimeInMillis
        ).map {
            UnlockEvent(unlockTimeInMillis = it.counterUnpausedTimeInMillis)
        }
        val fakeLockEvents = counterPausedEventsRepository.getCounterPausedEventsSinceTime(
            sinceTimeInMillis = todayBeginningTimeInMillis
        ).map {
            LockEvent(lockTimeInMillis = it.counterPausedTimeInMillis)
        }

        val screenEvents = (unlockEvents + lockEvents + fakeUnlockEvents + fakeLockEvents)
            .sortedBy { it.timeInMillis }

        if (screenEvents.isEmpty()) {
            return getHoursAndMinutesDurationPair(
                durationTimeInMillis = currentTimeInMillis - todayBeginningTimeInMillis
            )
        }

        var screenTimeDuration = 0L
        var previousScreenEvent: ScreenEvent = screenEvents[0]

        if (previousScreenEvent is LockEvent) {
            screenTimeDuration += previousScreenEvent.timeInMillis - todayBeginningTimeInMillis
        }

        var previousSinceTime: Long =
            if (previousScreenEvent is UnlockEvent) previousScreenEvent.timeInMillis
            else todayBeginningTimeInMillis

        screenEvents.subList(1, screenEvents.size).forEach {
            if (it is UnlockEvent) {
                previousSinceTime = it.timeInMillis
            } else if (it is LockEvent) {
                if (previousScreenEvent !is LockEvent) {
                    screenTimeDuration += it.timeInMillis - previousSinceTime
                }
            }

            previousScreenEvent = it
        }

        if (previousScreenEvent is UnlockEvent) {
            screenTimeDuration += currentTimeInMillis - previousSinceTime
        }

        return getHoursAndMinutesDurationPair(
            durationTimeInMillis = screenTimeDuration
        )
    }

    private fun getHoursAndMinutesDurationPair(durationTimeInMillis: Long): Pair<Int, Int> {
        val hours = durationTimeInMillis / 3600000
        val minutes = (durationTimeInMillis % 3600000) / 60000

        return Pair(hours.toInt(), minutes.toInt())
    }
}