package com.sweak.unlockmaster.domain.use_case.unlock_events

import com.sweak.unlockmaster.domain.repository.UnlockEventsRepository
import javax.inject.Inject

class GetAllTimeDaysToUnlockEventCountsUseCase @Inject constructor(
    private val unlockEventsRepository: UnlockEventsRepository
) {
    suspend operator fun invoke(): List<Pair<Long, Int>> {
        return listOf(
            Pair(1680903896000, 37),
            Pair(1680903896000, 38),
            Pair(1680903896000, 36),
            Pair(1680903896000, 35),
            Pair(1680903896000, 35),
            Pair(1680903896000, 37),
            Pair(1680903896000, 34),
            Pair(1680903896000, 35),
            Pair(1680903896000, 36),
            Pair(1680903896000, 35),
            Pair(1680903896000, 33),
            Pair(1680903896000, 30),
            Pair(1680903896000, 31),
            Pair(1680903896000, 30)
        )
    }
}