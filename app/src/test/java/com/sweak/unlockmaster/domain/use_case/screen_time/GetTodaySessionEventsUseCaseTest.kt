package com.sweak.unlockmaster.domain.use_case.screen_time

import com.sweak.unlockmaster.data.repository.*
import com.sweak.unlockmaster.domain.model.*
import com.sweak.unlockmaster.domain.model.SessionEvent.*
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetTodaySessionEventsUseCaseTest {

    private lateinit var getTodaySessionEventsUseCase: GetTodaySessionEventsUseCase
    private lateinit var unlockEventsRepository: UnlockEventsRepositoryFake
    private lateinit var lockEventsRepository: LockEventsRepositoryFake
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
        getTodaySessionEventsUseCase = GetTodaySessionEventsUseCase(
            unlockEventsRepository,
            lockEventsRepository,
            counterPausedEventsRepository,
            counterUnpausedEventsRepository,
            timeRepository
        )
    }

    @Test
    fun `No screen events and it is 3 45 10 AM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676774710000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(ScreenTime(1676761200000, 1676774710000, 13510000)),
            getTodaySessionEventsUseCase()
        )
    }

    @Test
    fun `There is only one LockEvent and it is 1 00 PM`() = runTest {
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(lockTimeInMillis = 1676771745000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676808000000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(ScreenTime(1676761200000, 1676771745000, 10545000)),
            getTodaySessionEventsUseCase()
        )
    }

    @Test
    fun `There is only one UnlockEvent and it is 1 25 AM`() = runTest {
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(unlockTimeInMillis = 1676763330000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676766300000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(ScreenTime(1676763330000, 1676766300000, 2970000)),
            getTodaySessionEventsUseCase()
        )
    }

    @Test
    fun `There are three LockEvents and it is 1 00 PM`() = runTest {
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(lockTimeInMillis = 1676771745000),
            LockEvent(lockTimeInMillis = 1676774130000),
            LockEvent(lockTimeInMillis = 1676775383000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676808000000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(ScreenTime(1676761200000, 1676771745000, 10545000)),
            getTodaySessionEventsUseCase()
        )
    }

    @Test
    fun `There are three UnlockEvents and it is 1 25 AM`() = runTest {
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(unlockTimeInMillis = 1676761805000),
            UnlockEvent(unlockTimeInMillis = 1676762594000),
            UnlockEvent(unlockTimeInMillis = 1676763330000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676766300000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(ScreenTime(1676763330000, 1676766300000, 2970000)),
            getTodaySessionEventsUseCase()
        )
    }

    @Test
    fun `There is only one CounterPauseEvent and it is 2 30 30 AM`() = runTest {
        counterPausedEventsRepository.counterPausedEventsSinceTimeToBeReturned = listOf(
            CounterPausedEvent(counterPausedTimeInMillis = 1676764845000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676770230000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(
                ScreenTime(1676761200000, 1676764845000, 3645000),
                CounterPaused(1676764845000, 1676770230000)
            ),
            getTodaySessionEventsUseCase()
        )
    }

    @Test
    fun `There is only one CounterUnpauseEvent and it is 3 10 15 AM`() = runTest {
        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(counterUnpausedTimeInMillis = 1676766602000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676772615000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(
                CounterPaused(1676761200000, 1676766602000),
                ScreenTime(1676766602000, 1676772615000, 6013000)
            ),
            getTodaySessionEventsUseCase()
        )
    }

    @Test
    fun `There is specific sequence - U(CP)(CU)L and it is 7 00 AM`() = runTest {
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(unlockTimeInMillis = 1676782923000)
        )
        counterPausedEventsRepository.counterPausedEventsSinceTimeToBeReturned = listOf(
            CounterPausedEvent(counterPausedTimeInMillis = 1676782946000)
        )
        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(counterUnpausedTimeInMillis = 1676785601000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(lockTimeInMillis = 1676785619000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676786400000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(
                ScreenTime(1676782923000, 1676782946000, 23000),
                CounterPaused(1676782946000, 1676785601000),
                ScreenTime(1676785601000, 1676785619000, 18000)
            ),
            getTodaySessionEventsUseCase()
        )
    }

    @Test
    fun `There is a specific sequence - ULULUL and it is 7 34 08 PM`() = runTest {
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(unlockTimeInMillis = 1676799293000),
            UnlockEvent(unlockTimeInMillis = 1676809203000),
            UnlockEvent(unlockTimeInMillis = 1676822427000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(lockTimeInMillis = 1676800561000),
            LockEvent(lockTimeInMillis = 1676810207000),
            LockEvent(lockTimeInMillis = 1676825714000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676831648000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(
                ScreenTime(1676799293000, 1676800561000, 1268000),
                ScreenTime(1676809203000, 1676810207000, 1004000),
                ScreenTime(1676822427000, 1676825714000, 3287000)
            ),
            getTodaySessionEventsUseCase()
        )
    }

    @Test
    fun `There is a specific sequence - LULULU and it is 7 34 08 PM`() = runTest {
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(unlockTimeInMillis = 1676792173000),
            UnlockEvent(unlockTimeInMillis = 1676820020000),
            UnlockEvent(unlockTimeInMillis = 1676831642000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(lockTimeInMillis = 1676762480000),
            LockEvent(lockTimeInMillis = 1676793540000),
            LockEvent(lockTimeInMillis = 1676820226000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676831648000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        Assert.assertEquals(
            listOf(
                ScreenTime(1676761200000, 1676762480000, 1280000),
                ScreenTime(1676792173000, 1676793540000, 1367000),
                ScreenTime(1676820020000, 1676820226000, 206000),
                ScreenTime(1676831642000, 1676831648000, 6000)
            ),
            getTodaySessionEventsUseCase()
        )
    }
}
