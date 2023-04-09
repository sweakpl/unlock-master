package com.sweak.unlockmaster.domain.use_case.screen_time

import com.sweak.unlockmaster.data.repository.*
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetTodayScreenTimeDurationUseCaseTest {

    // All times in milliseconds should be referenced with the context of the timezone UTC +1.
    // E.g. when talking about todayBeginningTimeInMillis = 1676761200000 it means it is
    // Sun Feb 19 2023 00:00:00 which actually is the beginning of the day in the time zone UTC +1
    // while in the UTC +0 it is Sat Feb 18 2023 23:00:00.

    private lateinit var getTodayScreenTimeDurationUseCase: GetTodayScreenTimeDurationUseCase
    private lateinit var unlockEventsRepository: UnlockEventsRepositoryFake
    private lateinit var lockEventsRepository: LockEventsRepositoryFake
    private lateinit var counterPausedEventsRepository: CounterPausedEventsRepositoryFake
    private lateinit var counterUnpausedEventsRepository: CounterUnpausedEventsRepositoryFake
    private lateinit var timeRepository: TimeRepositoryFake
    private lateinit var userSessionRepository: UserSessionRepositoryFake

    @Before
    fun setUp() {
        unlockEventsRepository = UnlockEventsRepositoryFake()
        lockEventsRepository = LockEventsRepositoryFake()
        counterPausedEventsRepository = CounterPausedEventsRepositoryFake()
        counterUnpausedEventsRepository = CounterUnpausedEventsRepositoryFake()
        timeRepository = TimeRepositoryFake()
        userSessionRepository = UserSessionRepositoryFake()
        getTodayScreenTimeDurationUseCase = GetTodayScreenTimeDurationUseCase(
            unlockEventsRepository,
            lockEventsRepository,
            counterPausedEventsRepository,
            counterUnpausedEventsRepository,
            timeRepository,
            userSessionRepository
        )
    }

    @Test
    fun `If there are no screen events, then returns 2 hours and 45 minutes`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676771100000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(9900000, getTodayScreenTimeDurationUseCase())
    }

    @Test
    fun `If there are no screen events and unlock counter is paused, then returns 0 hours and 0 minutes`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676771100000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000
        userSessionRepository.isUnlockCounterPausedToBeReturned = true

        Assert.assertEquals(0L, getTodayScreenTimeDurationUseCase())
    }

    @Test
    fun `If there is only one UnlockEvent, then returns 0 hours and 30 minutes`() = runTest {
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(unlockTimeInMillis = 1676835000000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(1800000, getTodayScreenTimeDurationUseCase())
    }

    @Test
    fun `If there is only one LockEvent, then returns 0 hours and 20 minutes`() = runTest {
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(lockTimeInMillis = 1676762400000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(1200000, getTodayScreenTimeDurationUseCase())
    }

    @Test
    fun `If there are only three UnlockEvents, then returns 0 hours and 15 minutes`() = runTest {
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(unlockTimeInMillis = 1676835000000),
            UnlockEvent(unlockTimeInMillis = 1676835600000),
            UnlockEvent(unlockTimeInMillis = 1676835900000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(900000, getTodayScreenTimeDurationUseCase())
    }

    @Test
    fun `If there are only three LockEvents, then returns 0 hours and 10 minutes`() = runTest {
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(lockTimeInMillis = 1676761800000),
            LockEvent(lockTimeInMillis = 1676763000000),
            LockEvent(lockTimeInMillis = 1676764800000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(600000, getTodayScreenTimeDurationUseCase())
    }

    @Test
    fun `If there is specific sequence - ULULULUL, then returns 1 hour and 15 minutes`() = runTest {
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
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(4500000, getTodayScreenTimeDurationUseCase())
    }

    @Test
    fun `If there is specific sequence - UUULULULUL, then returns 1 hour and 15 minutes`() = runTest {
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
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(4500000, getTodayScreenTimeDurationUseCase())
    }

    @Test
    fun `If there is specific sequence - ULULULULLL, then returns 1 hour and 15 minutes`() = runTest {
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
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(4500000, getTodayScreenTimeDurationUseCase())
    }

    @Test
    fun `If there is specific sequence - ULULUUULUL, then returns 1 hour and 30 minutes`() = runTest {
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
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(5400000, getTodayScreenTimeDurationUseCase())
    }

    @Test
    fun `If there is specific sequence - ULULLLULUL, then returns 0 hours and 55 minutes`() = runTest {
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

        Assert.assertEquals(3300000, getTodayScreenTimeDurationUseCase())
    }

    @Test
    fun `If there is only one CounterPausedEvent, then returns 3 hours and 15 minutes`() = runTest {
        counterPausedEventsRepository.counterPausedEventsSinceTimeToBeReturned = listOf(
            CounterPausedEvent(counterPausedTimeInMillis = 1676772900000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(11700000, getTodayScreenTimeDurationUseCase())
    }

    @Test
    fun `If there is only one CounterUnpausedEvent, then returns 1 hours and 15 minutes`() = runTest {
        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(counterUnpausedTimeInMillis = 1676832300000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(4500000, getTodayScreenTimeDurationUseCase())
    }

    @Test
    fun `If there is specific sequence - ULULU(CP)(CU)LUL, then returns 1 hour and 15 minutes`() = runTest {
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
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(4500000, getTodayScreenTimeDurationUseCase())
    }

    @Test
    fun `If there is specific sequence - U(CP)(CU)L, then returns 0 hours and 20 minutes`() = runTest {
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
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(1200000, getTodayScreenTimeDurationUseCase())
    }
}