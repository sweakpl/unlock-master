package com.sweak.unlockmaster.domain.use_case.lock_events

import com.sweak.unlockmaster.domain.repository.LockEventsRepository
import com.sweak.unlockmaster.domain.repository.TimeRepository
import javax.inject.Inject

class AddLockEventUseCase @Inject constructor(
    private val lockEventsRepository: LockEventsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke() {
        lockEventsRepository.addLockEvent(
            lockEventTimeInMillis = timeRepository.getCurrentTimeInMillis()
        )
    }
}