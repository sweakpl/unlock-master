package com.sweak.unlockmaster.domain.use_case.screen_time

import com.sweak.unlockmaster.domain.repository.*
import javax.inject.Inject

class GetTodayHourlyUsageMinutesUseCase @Inject constructor(
    private val unlockEventsRepository: UnlockEventsRepository,
    private val lockEventsRepository: LockEventsRepository,
    private val counterPausedEventsRepository: CounterPausedEventsRepository,
    private val counterUnpausedEventsRepository: CounterUnpausedEventsRepository,
    private val timeRepository: TimeRepository
) {

    suspend operator fun invoke(): List<Int> {
        return listOf(
            0,
            0,
            0,
            4,
            12,
            0,
            0,
            0,
            23,
            45,
            3,
            0,
            0,
            28,
            12,
            2,
            4,
            0,
            0,
            56,
            34,
            4,
            0,
            0
        )
    }
}