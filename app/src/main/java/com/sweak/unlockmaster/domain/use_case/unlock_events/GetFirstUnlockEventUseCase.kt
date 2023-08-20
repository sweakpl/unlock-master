package com.sweak.unlockmaster.domain.use_case.unlock_events

import com.sweak.unlockmaster.domain.model.UnlockMasterEvent
import com.sweak.unlockmaster.domain.repository.UnlockEventsRepository
import javax.inject.Inject

class GetFirstUnlockEventUseCase @Inject constructor(
    private val unlockEventsRepository: UnlockEventsRepository
) {
    suspend operator fun invoke(): UnlockMasterEvent.UnlockEvent? {
        return unlockEventsRepository.getFirstUnlockEvent()
    }
}