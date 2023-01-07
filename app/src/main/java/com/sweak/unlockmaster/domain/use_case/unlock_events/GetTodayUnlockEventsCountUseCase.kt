package com.sweak.unlockmaster.domain.use_case.unlock_events

import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.repository.UnlockEventsRepository
import javax.inject.Inject

class GetTodayUnlockEventsCountUseCase @Inject constructor(
    private val unlockEventsRepository: UnlockEventsRepository,
    private val timeRepository: TimeRepository
) {
    suspend operator fun invoke(): Int {
        val unlockEventsCount = unlockEventsRepository.getUnlockEventsCountSinceTime(
            timeRepository.getTodayBeginningTimeInMillis()
        )

        return if (unlockEventsCount < 0) 0 else unlockEventsCount
    }
}