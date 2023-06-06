package com.sweak.unlockmaster.domain.use_case.screen_time

import com.sweak.unlockmaster.data.repository.CounterPausedEventsRepositoryFake
import com.sweak.unlockmaster.data.repository.CounterUnpausedEventsRepositoryFake
import com.sweak.unlockmaster.data.repository.LockEventsRepositoryFake
import com.sweak.unlockmaster.data.repository.TimeRepositoryFake
import com.sweak.unlockmaster.data.repository.UnlockEventsRepositoryFake
import com.sweak.unlockmaster.domain.model.SessionEvent
import com.sweak.unlockmaster.domain.model.SessionEvent.*
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetSessionEventsForGivenDayUseCaseTest {

    // All times in milliseconds should be referenced with the context of the timezone UTC +1.
    // E.g. when talking about todayBeginningTimeInMillis = 1676761200000 it means it is
    // Sun Feb 19 2023 00:00:00 which actually is the beginning of the day in the time zone UTC +1
    // while in the UTC +0 it is Sat Feb 18 2023 23:00:00.

    private lateinit var getSessionEventsForGivenDayUseCase: GetSessionEventsForGivenDayUseCase
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
        getSessionEventsForGivenDayUseCase = GetSessionEventsForGivenDayUseCase(
            unlockEventsRepository,
            lockEventsRepository,
            counterPausedEventsRepository,
            counterUnpausedEventsRepository,
            timeRepository
        )
    }

    @Test
    fun `There are no screen events in the given and previous days, given day is the current day and it is 3 45 10 AM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676774710000

        Assert.assertEquals(
            listOf(ScreenTime(1676761200000, 1676774710000, 13510000)),
            getSessionEventsForGivenDayUseCase(1676774710000)
        )
    }

    @Test
    fun `There are no screen events in the given and previous days and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678764600000

        Assert.assertEquals(
            emptyList<SessionEvent>(),
            getSessionEventsForGivenDayUseCase(1676774710000)
        )
    }

    @Test
    fun `UnlockEvent is the latest event from previous day, there are no given day events, given day is the current day and it is 4 30 AM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678764600000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1678746645000)
        )

        Assert.assertEquals(
            listOf(ScreenTime(1678748400000, 1678764600000, 16200000)),
            getSessionEventsForGivenDayUseCase(1678764600000)
        )
    }

    @Test
    fun `UnlockEvent is the latest event from previous day, there are no given day events and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678975800000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1678746645000)
        )

        Assert.assertEquals(
            listOf(ScreenTime(1678748400000, 1678834800000, 86400000)),
            getSessionEventsForGivenDayUseCase(1678764600000)
        )
    }

    @Test
    fun `LockEvent is the latest event from previous day, there are no given day events, given day is the current day and it is 0 45 AM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678751100000

        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1678746645000)
        )

        Assert.assertEquals(
            emptyList<SessionEvent>(),
            getSessionEventsForGivenDayUseCase(1678751100000)
        )
    }

    @Test
    fun `LockEvent is the latest event from previous day, there are no given day events and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678975800000

        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1678746645000)
        )

        Assert.assertEquals(
            emptyList<SessionEvent>(),
            getSessionEventsForGivenDayUseCase(1678751100000)
        )
    }

    @Test
    fun `CounterPausedEvent is the latest event from previous day, there are no given day events, given day is the current day and it is 3 45 10 AM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676774710000

        counterPausedEventsRepository.counterPausedEventsSinceTimeToBeReturned = listOf(
            CounterPausedEvent(1676760350000)
        )

        Assert.assertEquals(
            listOf(CounterPaused(1676761200000, 1676774710000)),
            getSessionEventsForGivenDayUseCase(1676774710000)
        )
    }

    @Test
    fun `CounterPausedEvent is the latest event from previous day, there are no given day events and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678746645000

        counterPausedEventsRepository.counterPausedEventsSinceTimeToBeReturned = listOf(
            CounterPausedEvent(1676760350000)
        )

        Assert.assertEquals(
            listOf(CounterPaused(1676761200000, 1676847600000)),
            getSessionEventsForGivenDayUseCase(1676774710000)
        )
    }

    @Test
    fun `CounterUnpausedEvent is the latest event from previous day, there are no given day events, given day is the current day and it is 4 30 AM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678764600000

        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(1678746645000)
        )

        Assert.assertEquals(
            listOf(ScreenTime(1678748400000, 1678764600000, 16200000)),
            getSessionEventsForGivenDayUseCase(1678764600000)
        )
    }

    @Test
    fun `CounterUnpausedEvent is the latest event from previous day, there are no given day events and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678975800000

        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(1678746645000)
        )

        Assert.assertEquals(
            listOf(ScreenTime(1678748400000, 1678834800000, 86400000)),
            getSessionEventsForGivenDayUseCase(1678764600000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one UnlockEvent in the given day, given day is the current day and it is 1 25 AM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676766300000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676763330000)
        )

        Assert.assertEquals(
            listOf(ScreenTime(1676763330000, 1676766300000, 2970000)),
            getSessionEventsForGivenDayUseCase(1676766300000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one UnlockEvent in the given day and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678764600000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676763330000)
        )

        Assert.assertEquals(
            listOf(ScreenTime(1676763330000, 1676847600000, 84270000)),
            getSessionEventsForGivenDayUseCase(1676766300000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one LockEvent in the given day, given day is the current day and it is 1 00 PM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676808000000

        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676771745000)
        )

        Assert.assertEquals(
            listOf(ScreenTime(1676761200000, 1676771745000, 10545000)),
            getSessionEventsForGivenDayUseCase(1676808000000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one LockEvent in the given day and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678975800000

        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676771745000)
        )

        Assert.assertEquals(
            listOf(ScreenTime(1676761200000, 1676771745000, 10545000)),
            getSessionEventsForGivenDayUseCase(1676808000000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one CounterUnpausedEvent in the given day, given day is the current day and it is 3 10 15 AM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676772615000

        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(1676766602000)
        )

        Assert.assertEquals(
            listOf(
                CounterPaused(1676761200000, 1676766602000),
                ScreenTime(1676766602000, 1676772615000, 6013000)
            ),
            getSessionEventsForGivenDayUseCase(1676772615000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one CounterUnpausedEvent in the given day and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678975800000

        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(1676766602000)
        )

        Assert.assertEquals(
            listOf(
                CounterPaused(1676761200000, 1676766602000),
                ScreenTime(1676766602000, 1676847600000, 80998000)
            ),
            getSessionEventsForGivenDayUseCase(1676772615000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one CounterPausedEvent in the given day, given day is the current day and it is 2 30 30 AM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676770230000

        counterPausedEventsRepository.counterPausedEventsSinceTimeToBeReturned = listOf(
            CounterPausedEvent(1676764845000)
        )

        Assert.assertEquals(
            listOf(
                ScreenTime(1676761200000, 1676764845000, 3645000),
                CounterPaused(1676764845000, 1676770230000)
            ),
            getSessionEventsForGivenDayUseCase(1676770230000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one CounterPausedEvent in the given day and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678746645000

        counterPausedEventsRepository.counterPausedEventsSinceTimeToBeReturned = listOf(
            CounterPausedEvent(1676764845000)
        )

        Assert.assertEquals(
            listOf(
                ScreenTime(1676761200000, 1676764845000, 3645000),
                CounterPaused(1676764845000, 1676847600000)
            ),
            getSessionEventsForGivenDayUseCase(1676770230000)
        )
    }

    @Test
    fun `There are no events from previous day, there are only three UnlockEvents in the given day, given day is the current day and it is 1 25 AM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676766300000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676761805000),
            UnlockEvent(1676762594000),
            UnlockEvent(1676763330000)
        )

        Assert.assertEquals(
            listOf(ScreenTime(1676763330000, 1676766300000, 2970000)),
            getSessionEventsForGivenDayUseCase(1676766300000)
        )
    }

    @Test
    fun `There are no events from previous day, there are only three UnlockEvents in the given day and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678975800000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676761805000),
            UnlockEvent(1676762594000),
            UnlockEvent(1676763330000)
        )

        Assert.assertEquals(
            listOf(ScreenTime(1676763330000, 1676847600000, 84270000)),
            getSessionEventsForGivenDayUseCase(1676766300000)
        )
    }

    @Test
    fun `There are no events from previous day, there are only three LockEvents in the given day, given day is the current day and it is 1 00 PM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676808000000

        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676771745000),
            LockEvent(1676774130000),
            LockEvent(1676775383000)
        )

        Assert.assertEquals(
            listOf(ScreenTime(1676761200000, 1676771745000, 10545000)),
            getSessionEventsForGivenDayUseCase(1676808000000)
        )
    }

    @Test
    fun `There are no events from previous day, there are only three LockEvents in the given day and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678746645000

        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676771745000),
            LockEvent(1676774130000),
            LockEvent(1676775383000)
        )

        Assert.assertEquals(
            listOf(ScreenTime(1676761200000, 1676771745000, 10545000)),
            getSessionEventsForGivenDayUseCase(1676808000000)
        )
    }

    @Test
    fun `There are no events from previous day, there is specific sequence - U(CP)(CU)L in the given day, given day is the current day and it is 7 00 AM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676786400000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676782923000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676785619000)
        )
        counterPausedEventsRepository.counterPausedEventsSinceTimeToBeReturned = listOf(
            CounterPausedEvent(1676782946000)
        )
        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(1676785601000)
        )

        Assert.assertEquals(
            listOf(
                ScreenTime(1676782923000, 1676782946000, 23000),
                CounterPaused(1676782946000, 1676785601000),
                ScreenTime(1676785601000, 1676785619000, 18000)
            ),
            getSessionEventsForGivenDayUseCase(1676786400000)
        )
    }

    @Test
    fun `There are no events from previous day, there is specific sequence - ULU(CP) in the given day and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678975800000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676762423000),
            UnlockEvent(1676768362000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676763362000)
        )
        counterPausedEventsRepository.counterPausedEventsSinceTimeToBeReturned = listOf(
            CounterPausedEvent(1676769172000)
        )

        Assert.assertEquals(
            listOf(
                ScreenTime(1676762423000, 1676763362000, 939000),
                ScreenTime(1676768362000, 1676769172000, 810000),
                CounterPaused(1676769172000, 1676847600000)
            ),
            getSessionEventsForGivenDayUseCase(1676770230000)
        )
    }

    @Test
    fun `CounterPausedEvent is the latest event from previous day, there is specific sequence - (CU)LU in the given day, given day is the current day and it is 3 10 15 AM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676772615000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676771919000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676767341000)
        )
        counterPausedEventsRepository.counterPausedEventsSinceTimeToBeReturned = listOf(
            CounterPausedEvent(1676760350000)
        )
        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(1676766602000)
        )

        Assert.assertEquals(
            listOf(
                CounterPaused(1676761200000, 1676766602000),
                ScreenTime(1676766602000, 1676767341000, 739000),
                ScreenTime(1676771919000, 1676772615000, 696000)
            ),
            getSessionEventsForGivenDayUseCase(1676772615000)
        )
    }

    @Test
    fun `LockEvent is the latest event from previous day, there is a specific sequence - ULULUL in the given day, given day is the current day and it is 7 34 08 PM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676831648000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676799293000),
            UnlockEvent(1676809203000),
            UnlockEvent(1676822427000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676760350000),

            LockEvent(1676800561000),
            LockEvent(1676810207000),
            LockEvent(1676825714000)
        )

        Assert.assertEquals(
            listOf(
                ScreenTime(1676799293000, 1676800561000, 1268000),
                ScreenTime(1676809203000, 1676810207000, 1004000),
                ScreenTime(1676822427000, 1676825714000, 3287000)
            ),
            getSessionEventsForGivenDayUseCase(1676831648000)
        )
    }

    @Test
    fun `CounterUnpausedEvent is the latest event from previous day, there is a specific sequence - LULULU in the given day and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678975800000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676792173000),
            UnlockEvent(1676820020000),
            UnlockEvent(1676831642000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676762480000),
            LockEvent(1676793540000),
            LockEvent(1676820226000)
        )
        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(1676760350000)
        )

        Assert.assertEquals(
            listOf(
                ScreenTime(1676761200000, 1676762480000, 1280000),
                ScreenTime(1676792173000, 1676793540000, 1367000),
                ScreenTime(1676820020000, 1676820226000, 206000),
                ScreenTime(1676831642000, 1676847600000, 15958000)
            ),
            getSessionEventsForGivenDayUseCase(1676831648000)
        )
    }

    @Test
    fun `UnlockEvent is the latest event from previous day, there is a specific sequence - LULULU in the given day, given day is the current day and it is 7 34 08 PM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676831648000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676760350000),

            UnlockEvent(1676792173000),
            UnlockEvent(1676820020000),
            UnlockEvent(1676831642000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676762480000),
            LockEvent(1676793540000),
            LockEvent(1676820226000)
        )

        Assert.assertEquals(
            listOf(
                ScreenTime(1676761200000, 1676762480000, 1280000),
                ScreenTime(1676792173000, 1676793540000, 1367000),
                ScreenTime(1676820020000, 1676820226000, 206000),
                ScreenTime(1676831642000, 1676831648000, 6000)
            ),
            getSessionEventsForGivenDayUseCase(1676831648000)
        )
    }
}