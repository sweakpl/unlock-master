package com.sweak.unlockmaster.domain.use_case.screen_time

import com.sweak.unlockmaster.data.repository.CounterPausedEventsRepositoryFake
import com.sweak.unlockmaster.data.repository.CounterUnpausedEventsRepositoryFake
import com.sweak.unlockmaster.data.repository.LockEventsRepositoryFake
import com.sweak.unlockmaster.data.repository.TimeRepositoryFake
import com.sweak.unlockmaster.data.repository.UnlockEventsRepositoryFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetHourlyUsageMinutesForGivenDayUseCaseTest {

    // All times in milliseconds should be referenced with the context of the timezone UTC +1.
    // E.g. when talking about todayBeginningTimeInMillis = 1676761200000 it means it is
    // Sun Feb 19 2023 00:00:00 which actually is the beginning of the day in the time zone UTC +1
    // while in the UTC +0 it is Sat Feb 18 2023 23:00:00.

    private lateinit var getHourlyUsageMinutesForGivenDayUseCase: GetHourlyUsageMinutesForGivenDayUseCase
    private lateinit var lockEventsRepository: LockEventsRepositoryFake
    private lateinit var unlockEventsRepository: UnlockEventsRepositoryFake
    private lateinit var counterPausedEventsRepository: CounterPausedEventsRepositoryFake
    private lateinit var counterUnpausedEventsRepository: CounterUnpausedEventsRepositoryFake
    private lateinit var timeRepository: TimeRepositoryFake

    @Before
    fun setUp() {
        unlockEventsRepository = UnlockEventsRepositoryFake()
        lockEventsRepository = LockEventsRepositoryFake()
        counterPausedEventsRepository = CounterPausedEventsRepositoryFake()
        counterUnpausedEventsRepository = CounterUnpausedEventsRepositoryFake()
        timeRepository = TimeRepositoryFake()
        getHourlyUsageMinutesForGivenDayUseCase = GetHourlyUsageMinutesForGivenDayUseCase(
            unlockEventsRepository,
            lockEventsRepository,
            counterPausedEventsRepository,
            counterUnpausedEventsRepository,
            timeRepository
        )
    }

    @Test
    fun `test name`() = runTest {

    }
}