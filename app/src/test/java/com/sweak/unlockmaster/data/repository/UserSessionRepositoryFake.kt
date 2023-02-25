package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.domain.repository.UserSessionRepository

class UserSessionRepositoryFake : UserSessionRepository {

    var isUnlockCounterPausedToBeReturned: Boolean = false

    override suspend fun setIntroductionFinished() {
        TODO("Not yet implemented")
    }

    override suspend fun isIntroductionFinished(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun setUnlockCounterPaused(isPaused: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun isUnlockCounterPaused(): Boolean {
        return isUnlockCounterPausedToBeReturned
    }
}