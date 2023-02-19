package com.sweak.unlockmaster.domain.use_case.screen_time

import com.sweak.unlockmaster.domain.model.LockEvent
import com.sweak.unlockmaster.domain.model.ScreenEvent
import com.sweak.unlockmaster.domain.model.UnlockEvent
import com.sweak.unlockmaster.domain.repository.LockEventsRepository
import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.repository.UnlockEventsRepository
import javax.inject.Inject

class GetTodayHoursAndMinutesScreenTimePair @Inject constructor(
    private val unlockEventsRepository: UnlockEventsRepository,
    private val lockEventsRepository: LockEventsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke(): Pair<Int, Int> {
        val todayBeginningTimeInMillis = timeRepository.getTodayBeginningTimeInMillis()

        val unlockEvents = unlockEventsRepository.getUnlockEventsSinceTime(
            sinceTimeInMillis = todayBeginningTimeInMillis
        )
        val lockEvents = lockEventsRepository.getLockEventsSinceTime(
            sinceTimeInMillis = todayBeginningTimeInMillis
        )
        val screenEvents = (unlockEvents + lockEvents).sortedBy { it.timeInMillis }

        if (screenEvents.isEmpty()) {
            return Pair(0, 0)
        }

        var screenTimeDuration = 0L
        var previousScreenEvent: ScreenEvent = screenEvents[0]
        var previousSinceTime: Long = todayBeginningTimeInMillis

        if (previousScreenEvent is LockEvent && screenEvents.size == 1) {
            screenTimeDuration += previousScreenEvent.timeInMillis - previousSinceTime
        }

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
            screenTimeDuration += timeRepository.getCurrentTimeInMillis() - previousSinceTime
        }

        val hours = screenTimeDuration / 3600000
        val minutes = (screenTimeDuration % 3600000) / 60000

        return Pair(hours.toInt(), minutes.toInt())
    }
}