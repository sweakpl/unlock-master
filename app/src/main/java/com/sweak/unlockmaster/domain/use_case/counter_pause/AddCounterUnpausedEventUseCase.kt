package com.sweak.unlockmaster.domain.use_case.counter_pause

import com.sweak.unlockmaster.domain.model.UnlockMasterEvent
import com.sweak.unlockmaster.domain.repository.CounterUnpausedEventsRepository
import com.sweak.unlockmaster.domain.repository.TimeRepository
import javax.inject.Inject

class AddCounterUnpausedEventUseCase @Inject constructor(
    private val counterUnpausedEventsRepository: CounterUnpausedEventsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke() {
        counterUnpausedEventsRepository.addCounterUnpausedEvent(
            counterUnpausedEvent = UnlockMasterEvent.CounterUnpausedEvent(
                counterUnpausedTimeInMillis = timeRepository.getCurrentTimeInMillis()
            )
        )
    }
}