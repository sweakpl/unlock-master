package com.sweak.unlockmaster.domain.use_case.counter_pause

import com.sweak.unlockmaster.domain.model.UnlockMasterEvent
import com.sweak.unlockmaster.domain.repository.CounterPausedEventsRepository
import com.sweak.unlockmaster.domain.repository.TimeRepository
import javax.inject.Inject

class AddCounterPausedEventUseCase @Inject constructor(
    private val counterPausedEventsRepository: CounterPausedEventsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke() {
        counterPausedEventsRepository.addCounterPausedEvent(
            counterPausedEvent = UnlockMasterEvent.CounterPausedEvent(
                counterPausedTimeInMillis = timeRepository.getCurrentTimeInMillis()
            )
        )
    }
}