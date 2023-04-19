package com.sweak.unlockmaster.domain.use_case.unlock_events

import com.sweak.unlockmaster.domain.model.UnlockMasterEvent
import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.repository.UnlockEventsRepository
import javax.inject.Inject

class AddUnlockEventUseCase @Inject constructor(
    private val unlockEventsRepository: UnlockEventsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke() {
        unlockEventsRepository.addUnlockEvent(
            unlockEvent = UnlockMasterEvent.UnlockEvent(
                unlockTimeInMillis = timeRepository.getCurrentTimeInMillis()
            )
        )
    }
}