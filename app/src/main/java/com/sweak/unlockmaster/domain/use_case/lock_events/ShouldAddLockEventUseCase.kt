package com.sweak.unlockmaster.domain.use_case.lock_events

import com.sweak.unlockmaster.domain.repository.ScreenOnEventsRepository
import com.sweak.unlockmaster.domain.repository.UnlockEventsRepository
import javax.inject.Inject
import kotlin.math.abs

class ShouldAddLockEventUseCase @Inject constructor(
    private val unlockEventsRepository: UnlockEventsRepository,
    private val screenOnEventsRepository: ScreenOnEventsRepository
) {
    suspend operator fun invoke(): Boolean {
        val latestUnlockEvent = unlockEventsRepository.getLatestUnlockEvent()
        val latestScreenOnEvent = screenOnEventsRepository.getLatestScreenOnEvent()

        if (latestUnlockEvent == null) {
            return false
        }

        if (latestScreenOnEvent == null) {
            return true
        }

        val unlockAndScreenOnDifferenceTimeInMillis =
            latestUnlockEvent.timeInMillis - latestScreenOnEvent.timeInMillis

        // The latest event is clearly an UnlockEvent so we can safely return true to add LockEvent.
        if (unlockAndScreenOnDifferenceTimeInMillis > 0) {
            return true
        }

        // When instant-unlocking (unlocking without screen interaction e.g. fingerprint unlock)
        // UnlockEvents are usually recorded BEFORE ScreenOnEvents.
        // In an instant-unlock cases, the highest time between UnlockEvent and ScreenOnEvent
        // recorded was ~2 seconds. With that in mind, if we have a time between first UnlockEvent
        // and second ScreenOnEvent that is less than 2 seconds we consider the last UnlockEvent
        // being and instant-unlock and thus allowing to add LockEvent by returning true.
        return abs(unlockAndScreenOnDifferenceTimeInMillis) <= 2000L
    }
}