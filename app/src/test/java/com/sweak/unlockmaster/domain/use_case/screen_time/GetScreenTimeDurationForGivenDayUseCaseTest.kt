package com.sweak.unlockmaster.domain.use_case.screen_time

import com.sweak.unlockmaster.data.repository.CounterPausedEventsRepositoryFake
import com.sweak.unlockmaster.data.repository.CounterUnpausedEventsRepositoryFake
import com.sweak.unlockmaster.data.repository.LockEventsRepositoryFake
import com.sweak.unlockmaster.data.repository.TimeRepositoryFake
import com.sweak.unlockmaster.data.repository.UnlockEventsRepositoryFake
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetScreenTimeDurationForGivenDayUseCaseTest {

    // All times in milliseconds should be referenced with the context of the timezone UTC +1.
    // E.g. when talking about currentTimeInMillis = 1676761200000 it means it is
    // Sun Feb 19 2023 00:00:00 which actually is the beginning of the day in the time zone UTC +1
    // while in the UTC +0 it is Sat Feb 18 2023 23:00:00.

    private lateinit var getScreenTimeDurationForGivenDayUseCase: GetScreenTimeDurationForGivenDayUseCase
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
        getScreenTimeDurationForGivenDayUseCase = GetScreenTimeDurationForGivenDayUseCase(
            unlockEventsRepository,
            lockEventsRepository,
            counterPausedEventsRepository,
            counterUnpausedEventsRepository,
            timeRepository
        )
    }

    @Test
    fun `There are no screen events in the given and previous days and given day is the current day, returns 9900000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676771100000

        Assert.assertEquals(
            9900000,
            getScreenTimeDurationForGivenDayUseCase(1676809255000)
        )
    }

    @Test
    fun `There are no screen events in the given and previous days and given day is not the current day, returns 0 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676979176000

        Assert.assertEquals(
            0,
            getScreenTimeDurationForGivenDayUseCase(1676809255000)
        )
    }

    @Test
    fun `UnlockEvent is the latest event from previous day, there are no given day events and given day is the current day, returns 5445000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676766645000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676760683000)
        )

        Assert.assertEquals(
            5445000,
            getScreenTimeDurationForGivenDayUseCase(1676809255000)
        )
    }

    @Test
    fun `UnlockEvent is the latest event from previous day, there are no given day events and given day is not the current day, returns 86400000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676979176000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676760683000)
        )

        Assert.assertEquals(
            86400000,
            getScreenTimeDurationForGivenDayUseCase(1676809255000)
        )
    }

    @Test
    fun `LockEvent is the latest event from previous day, there are no given day events and given day is the current day, returns 0 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676766645000

        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676760683000)
        )

        Assert.assertEquals(
            0,
            getScreenTimeDurationForGivenDayUseCase(1676809255000)
        )
    }

    @Test
    fun `LockEvent is the latest event from previous day, there are no given day events and given day is not the current day, returns 0 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676979176000

        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676760683000)
        )

        Assert.assertEquals(
            0,
            getScreenTimeDurationForGivenDayUseCase(1676809255000)
        )
    }

    @Test
    fun `CounterPausedEvent is the latest event from previous day, there are no given day events and given day is the current day, returns 0 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676766645000

        counterPausedEventsRepository.counterPausedEventsSinceTimeToBeReturned = listOf(
            CounterPausedEvent(1676760683000)
        )

        Assert.assertEquals(
            0,
            getScreenTimeDurationForGivenDayUseCase(1676809255000)
        )
    }

    @Test
    fun `CounterPausedEvent is the latest event from previous day, there are no given day events and given day is not the current day, returns 0 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676979176000

        counterPausedEventsRepository.counterPausedEventsSinceTimeToBeReturned = listOf(
            CounterPausedEvent(1676760683000)
        )

        Assert.assertEquals(
            0,
            getScreenTimeDurationForGivenDayUseCase(1676809255000)
        )
    }

    @Test
    fun `CounterUnpausedEvent is the latest event from previous day, there are no given day events and given day is the current day, returns 5445000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676766645000

        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(1676760683000)
        )

        Assert.assertEquals(
            5445000,
            getScreenTimeDurationForGivenDayUseCase(1676809255000)
        )
    }

    @Test
    fun `CounterUnpausedEvent is the latest event from previous day, there are no given day events and given day is not the current day, returns 86400000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676979176000

        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(1676760683000)
        )

        Assert.assertEquals(
            86400000,
            getScreenTimeDurationForGivenDayUseCase(1676809255000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one UnlockEvent in the given day and given day is the current day, returns 538000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1679596738000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1679596200000)
        )

        Assert.assertEquals(
            538000,
            getScreenTimeDurationForGivenDayUseCase(1679596738000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one UnlockEvent in the given day and given day is not the current day, returns 16200000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1679852675000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1679596200000)
        )

        Assert.assertEquals(
            16200000,
            getScreenTimeDurationForGivenDayUseCase(1679596738000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one LockEvent in the given day and given day is the current day, returns 1590000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1679596738000

        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1679527590000)
        )

        Assert.assertEquals(
            1590000,
            getScreenTimeDurationForGivenDayUseCase(1679596738000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one LockEvent in the given day and given day is not the current day, returns 1590000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1679852675000

        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1679527590000)
        )

        Assert.assertEquals(
            1590000,
            getScreenTimeDurationForGivenDayUseCase(1679596738000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one CounterUnpausedEvent in the given day and given day is the current day, returns 538000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1679596738000

        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(1679596200000)
        )

        Assert.assertEquals(
            538000,
            getScreenTimeDurationForGivenDayUseCase(1679596738000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one CounterUnpausedEvent in the given day and given day is not the current day, returns 16200000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1679852675000

        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(1679596200000)
        )

        Assert.assertEquals(
            16200000,
            getScreenTimeDurationForGivenDayUseCase(1679596738000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one CounterPausedEvent in the given day and given day is the current day, returns 1590000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1679596738000

        counterPausedEventsRepository.counterPausedEventsSinceTimeToBeReturned = listOf(
            CounterPausedEvent(1679527590000)
        )

        Assert.assertEquals(
            1590000,
            getScreenTimeDurationForGivenDayUseCase(1679596738000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one CounterPausedEvent in the given day and given day is not the current day, returns 1590000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1679852675000

        counterPausedEventsRepository.counterPausedEventsSinceTimeToBeReturned = listOf(
            CounterPausedEvent(1679527590000)
        )

        Assert.assertEquals(
            1590000,
            getScreenTimeDurationForGivenDayUseCase(1679596738000)
        )
    }

    @Test
    fun `There are no events from previous day, there are three UnlockEvents in the given day and given day is the current day, returns 900000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676835000000),
            UnlockEvent(1676835600000),
            UnlockEvent(1676835900000)
        )

        Assert.assertEquals(
            900000,
            getScreenTimeDurationForGivenDayUseCase(1676836800000)
        )
    }

    @Test
    fun `There are no events from previous day, there are three UnlockEvents in the given day and given day is not the current day, returns 11700000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1677359976000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676835000000),
            UnlockEvent(1676835600000),
            UnlockEvent(1676835900000)
        )

        Assert.assertEquals(
            11700000,
            getScreenTimeDurationForGivenDayUseCase(1676836800000)
        )
    }

    @Test
    fun `There are no events from previous day, there are three LockEvents in the given day and given day is the current day, then returns 600000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000

        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676761800000),
            LockEvent(1676763000000),
            LockEvent(1676764800000)
        )

        Assert.assertEquals(
            600000,
            getScreenTimeDurationForGivenDayUseCase(1676836800000)
        )
    }

    @Test
    fun `There are no events from previous day, there are three LockEvents in the given day and given day is not the current day, then returns 600000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1677359976000

        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676761800000),
            LockEvent(1676763000000),
            LockEvent(1676764800000)
        )

        Assert.assertEquals(
            600000,
            getScreenTimeDurationForGivenDayUseCase(1676836800000)
        )
    }

    @Test
    fun `There are no events from previous day, there is specific sequence - ULULULUL in the given day and given day is the current day, then returns 4500000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676790000000),
            UnlockEvent(1676799300000),
            UnlockEvent(1676811000000),
            UnlockEvent(1676824200000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676791200000),
            LockEvent(1676801100000),
            LockEvent(1676811900000),
            LockEvent(1676824800000)
        )

        Assert.assertEquals(
            4500000,
            getScreenTimeDurationForGivenDayUseCase(1676836800000)
        )
    }

    @Test
    fun `There are no events from previous day, there is specific sequence - LULULUL in the given day and given day is the current day, then returns 33300000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676759445000),

            UnlockEvent(1676799300000),
            UnlockEvent(1676811000000),
            UnlockEvent(1676824200000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676791200000),
            LockEvent(1676801100000),
            LockEvent(1676811900000),
            LockEvent(1676824800000)
        )

        Assert.assertEquals(
            33300000,
            getScreenTimeDurationForGivenDayUseCase(1676836800000)
        )
    }

    @Test
    fun `LockEvent is the latest event from previous day, there is specific sequence - LULULUL in the given day and given day is not the current day, then returns 3300000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1677359976000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676799300000),
            UnlockEvent(1676811000000),
            UnlockEvent(1676824200000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676759430000),

            LockEvent(1676791200000),
            LockEvent(1676801100000),
            LockEvent(1676811900000),
            LockEvent(1676824800000)
        )

        Assert.assertEquals(
            3300000,
            getScreenTimeDurationForGivenDayUseCase(1676836800000)
        )
    }

    @Test
    fun `LockEvent is the latest event from previous day, there is specific sequence - ULULULU in the given day and given day is the current day, then returns 16500000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676790000000),
            UnlockEvent(1676799300000),
            UnlockEvent(1676811000000),
            UnlockEvent(1676824200000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676759445000),

            LockEvent(1676791200000),
            LockEvent(1676801100000),
            LockEvent(1676811900000)
        )

        Assert.assertEquals(
            16500000,
            getScreenTimeDurationForGivenDayUseCase(1676836800000)
        )
    }

    @Test
    fun `UnlockEvent is the latest event from previous day, there is specific sequence - ULULULU in the given day and given day is not the current day, then returns 27300000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1677359976000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676759430000),

            UnlockEvent(1676790000000),
            UnlockEvent(1676799300000),
            UnlockEvent(1676811000000),
            UnlockEvent(1676824200000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676791200000),
            LockEvent(1676801100000),
            LockEvent(1676811900000)
        )

        Assert.assertEquals(
            27300000,
            getScreenTimeDurationForGivenDayUseCase(1676836800000)
        )
    }

    @Test
    fun `There are no events from previous day, there is specific sequence - U(CP)(CU)L in the given day and given day is the current day, then returns 1800000 milliseconds`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676790000000)
        )
        counterPausedEventsRepository.counterPausedEventsSinceTimeToBeReturned = listOf(
            CounterPausedEvent(1676791200000)
        )
        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(1676824200000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676824800000)
        )

        Assert.assertEquals(
            1800000,
            getScreenTimeDurationForGivenDayUseCase(1676836800000)
        )
    }
}