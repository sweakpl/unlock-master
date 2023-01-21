package com.sweak.unlockmaster.domain.use_case

import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import javax.inject.Inject

class SetUnlockCounterPause @Inject constructor(
    private val userSessionRepository: UserSessionRepository
) {
    suspend operator fun invoke(isPaused: Boolean) {
        userSessionRepository.setIsUnlockCounterPaused(isPaused = isPaused)
    }
}