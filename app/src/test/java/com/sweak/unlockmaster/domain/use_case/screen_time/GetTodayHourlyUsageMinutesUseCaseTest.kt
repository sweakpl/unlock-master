package com.sweak.unlockmaster.domain.use_case.screen_time

import com.sweak.unlockmaster.data.repository.*
import com.sweak.unlockmaster.domain.model.CounterPausedEvent
import com.sweak.unlockmaster.domain.model.CounterUnpausedEvent
import com.sweak.unlockmaster.domain.model.LockEvent
import com.sweak.unlockmaster.domain.model.UnlockEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetTodayHourlyUsageMinutesUseCaseTest {

    private lateinit var getTodayHourlyUsageMinutesUseCase: GetTodayHourlyUsageMinutesUseCase
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
        getTodayHourlyUsageMinutesUseCase = GetTodayHourlyUsageMinutesUseCase(
            unlockEventsRepository,
            lockEventsRepository,
            counterPausedEventsRepository,
            counterUnpausedEventsRepository,
            timeRepository
        )
    }

    @Test
    fun `No screen events and it is 4 30 AM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678764600000

        Assert.assertEquals(
            listOf(60, 60, 60, 60, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            getTodayHourlyUsageMinutesUseCase()
        )
    }

    @Test
    fun `There is only one UnlockEvent and it is 9 00 PM`() = runTest {
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(unlockTimeInMillis = 1676835000000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 0, 0, 0, 0),
            getTodayHourlyUsageMinutesUseCase()
        )
    }

    @Test
    fun `There is only one LockEvent and it is 3 00 AM`() = runTest {
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(lockTimeInMillis = 1676762400000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676772000000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            getTodayHourlyUsageMinutesUseCase()
        )
    }

    @Test
    fun `There are only three UnlockEvents and it is 9 00 PM`() = runTest {
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(unlockTimeInMillis = 1676835000000),
            UnlockEvent(unlockTimeInMillis = 1676835600000),
            UnlockEvent(unlockTimeInMillis = 1676835900000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 0, 0, 0, 0),
            getTodayHourlyUsageMinutesUseCase()
        )
    }

    @Test
    fun `There are only three LockEvents and it is 6 45 AM`() = runTest {
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(lockTimeInMillis = 1676761800000),
            LockEvent(lockTimeInMillis = 1676763000000),
            LockEvent(lockTimeInMillis = 1676764800000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676785500000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            getTodayHourlyUsageMinutesUseCase()
        )
    }

    @Test
    fun `There is specific sequence - ULULULUL and it is 7 15 PM`() = runTest {
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(unlockTimeInMillis = 1676790000000),
            UnlockEvent(unlockTimeInMillis = 1676799300000),
            UnlockEvent(unlockTimeInMillis = 1676811000000),
            UnlockEvent(unlockTimeInMillis = 1676824200000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(lockTimeInMillis = 1676791200000),
            LockEvent(lockTimeInMillis = 1676801100000),
            LockEvent(lockTimeInMillis = 1676811900000),
            LockEvent(lockTimeInMillis = 1676824800000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676830500000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 20, 0, 25, 5, 0, 10, 5, 0, 0, 10, 0, 0, 0, 0, 0, 0),
            getTodayHourlyUsageMinutesUseCase()
        )
    }

    @Test
    fun `There is specific sequence - UUULULULUL and it is 8 00 PM`() = runTest {
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(unlockTimeInMillis = 1676786400000),
            UnlockEvent(unlockTimeInMillis = 1676788200000),
            UnlockEvent(unlockTimeInMillis = 1676790000000),
            UnlockEvent(unlockTimeInMillis = 1676799300000),
            UnlockEvent(unlockTimeInMillis = 1676811000000),
            UnlockEvent(unlockTimeInMillis = 1676824200000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(lockTimeInMillis = 1676791200000),
            LockEvent(lockTimeInMillis = 1676801100000),
            LockEvent(lockTimeInMillis = 1676811900000),
            LockEvent(lockTimeInMillis = 1676824800000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676790000000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 20, 0, 25, 5, 0, 10, 5, 0, 0, 10, 0, 0, 0, 0, 0, 0),
            getTodayHourlyUsageMinutesUseCase()
        )
    }

    @Test
    fun `There is specific sequence - ULULULULLL and it is 11 00 PM`() = runTest {
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(unlockTimeInMillis = 1676790000000),
            UnlockEvent(unlockTimeInMillis = 1676799300000),
            UnlockEvent(unlockTimeInMillis = 1676811000000),
            UnlockEvent(unlockTimeInMillis = 1676824200000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(lockTimeInMillis = 1676791200000),
            LockEvent(lockTimeInMillis = 1676801100000),
            LockEvent(lockTimeInMillis = 1676811900000),
            LockEvent(lockTimeInMillis = 1676824800000),
            LockEvent(lockTimeInMillis = 1676826000000),
            LockEvent(lockTimeInMillis = 1676827800000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676844000000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 20, 0, 25, 5, 0, 10, 5, 0, 0, 10, 0, 0, 0, 0, 0, 0),
            getTodayHourlyUsageMinutesUseCase()
        )
    }

    @Test
    fun `There is specific sequence - ULULUUULUL and it is 6 00 PM`() = runTest {
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(unlockTimeInMillis = 1676786400000),
            UnlockEvent(unlockTimeInMillis = 1676799300000),
            UnlockEvent(unlockTimeInMillis = 1676804400000),
            UnlockEvent(unlockTimeInMillis = 1676806200000),
            UnlockEvent(unlockTimeInMillis = 1676811000000),
            UnlockEvent(unlockTimeInMillis = 1676824200000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(lockTimeInMillis = 1676788200000),
            LockEvent(lockTimeInMillis = 1676801100000),
            LockEvent(lockTimeInMillis = 1676812200000),
            LockEvent(lockTimeInMillis = 1676824800000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676826000000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 30, 0, 0, 25, 5, 0, 10, 10, 0, 0, 10, 0, 0, 0, 0, 0, 0),
            getTodayHourlyUsageMinutesUseCase()
        )
    }

    @Test
    fun `There is specific sequence - ULULLLULUL and it is 9 00 PM`() = runTest {
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(unlockTimeInMillis = 1676790000000),
            UnlockEvent(unlockTimeInMillis = 1676799300000),
            UnlockEvent(unlockTimeInMillis = 1676811000000),
            UnlockEvent(unlockTimeInMillis = 1676824200000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(lockTimeInMillis = 1676790600000),
            LockEvent(lockTimeInMillis = 1676801100000),
            LockEvent(lockTimeInMillis = 1676802600000),
            LockEvent(lockTimeInMillis = 1676806200000),
            LockEvent(lockTimeInMillis = 1676811300000),
            LockEvent(lockTimeInMillis = 1676824800000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 25, 60, 30, 5, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0),
            getTodayHourlyUsageMinutesUseCase()
        )
    }

    @Test
    fun `There is only one CounterPausedEvent and it is 7 30 AM`() = runTest {
        counterPausedEventsRepository.counterPausedEventsSinceTimeToBeReturned = listOf(
            CounterPausedEvent(counterPausedTimeInMillis = 1676772900000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676788200000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(60, 60, 60, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            getTodayHourlyUsageMinutesUseCase()
        )
    }

    @Test
    fun `There is only one CounterUnpausedEvent and it is 9 00 PM`() = runTest {
        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(counterUnpausedTimeInMillis = 1676832300000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 60, 0, 0, 0),
            getTodayHourlyUsageMinutesUseCase()
        )
    }

    @Test
    fun `There is specific sequence - ULULU(CP)(CU)LUL and it is 9 15 PM`() = runTest {
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(unlockTimeInMillis = 1676790000000),
            UnlockEvent(unlockTimeInMillis = 1676799300000),
            UnlockEvent(unlockTimeInMillis = 1676811000000),
            UnlockEvent(unlockTimeInMillis = 1676824200000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(lockTimeInMillis = 1676791200000),
            LockEvent(lockTimeInMillis = 1676801100000),
            LockEvent(lockTimeInMillis = 1676814300000),
            LockEvent(lockTimeInMillis = 1676824800000)
        )
        counterPausedEventsRepository.counterPausedEventsSinceTimeToBeReturned = listOf(
            CounterPausedEvent(counterPausedTimeInMillis = 1676811600000)
        )
        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(counterUnpausedTimeInMillis = 1676814000000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676837700000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 20, 0, 25, 5, 0, 10, 5, 0, 0, 10, 0, 0, 0, 0, 0, 0),
            getTodayHourlyUsageMinutesUseCase()
        )
    }

    @Test
    fun `There is specific sequence - U(CP)(CU)L and it is 8 45 PM`() = runTest {
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(unlockTimeInMillis = 1676795400000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(lockTimeInMillis = 1676831400000)
        )
        counterPausedEventsRepository.counterPausedEventsSinceTimeToBeReturned = listOf(
            CounterPausedEvent(counterPausedTimeInMillis = 1676796000000)
        )
        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(counterUnpausedTimeInMillis = 1676830800000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676835900000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0),
            getTodayHourlyUsageMinutesUseCase()
        )
    }
}