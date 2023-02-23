package com.sweak.unlockmaster.domain.use_case.counter_pause

import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import javax.inject.Inject

class SetUnlockCounterPauseUseCase @Inject constructor(
    private val userSessionRepository: UserSessionRepository
) {
    suspend operator fun invoke(isPaused: Boolean) {
        userSessionRepository.setIsUnlockCounterPaused(isPaused = isPaused)
    }
}