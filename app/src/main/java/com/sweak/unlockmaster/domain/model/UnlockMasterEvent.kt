package com.sweak.unlockmaster.domain.model

sealed class UnlockMasterEvent(val timeInMillis: Long) {
    class UnlockEvent(unlockTimeInMillis: Long) : UnlockMasterEvent(unlockTimeInMillis)
    class LockEvent(lockTimeInMillis: Long) : UnlockMasterEvent(lockTimeInMillis)
    class ScreenOnEvent(screenOnTimeInMillis: Long) : UnlockMasterEvent(screenOnTimeInMillis)
    class CounterPausedEvent(counterPausedTimeInMillis: Long) :
        UnlockMasterEvent(counterPausedTimeInMillis)
    class CounterUnpausedEvent(counterUnpausedTimeInMillis: Long) :
        UnlockMasterEvent(counterUnpausedTimeInMillis)
}
