package com.sweak.unlockmaster.domain.use_case.screen_on_events

import com.sweak.unlockmaster.domain.repository.ScreenOnEventsRepository
import com.sweak.unlockmaster.domain.repository.TimeRepository
import javax.inject.Inject

class AddScreenOnEventUseCase @Inject constructor(
    private val screenOnEventsRepository: ScreenOnEventsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke() {
        screenOnEventsRepository.addScreenOnEvent(
            screenOnEventTimeInMillis = timeRepository.getCurrentTimeInMillis()
        )
    }
}