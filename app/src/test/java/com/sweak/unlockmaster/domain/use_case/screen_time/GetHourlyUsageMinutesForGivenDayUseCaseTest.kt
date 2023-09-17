package com.sweak.unlockmaster.domain.use_case.screen_time

import com.sweak.unlockmaster.data.repository.CounterPausedEventsRepositoryFake
import com.sweak.unlockmaster.data.repository.CounterUnpausedEventsRepositoryFake
import com.sweak.unlockmaster.data.repository.LockEventsRepositoryFake
import com.sweak.unlockmaster.data.repository.TimeRepositoryFake
import com.sweak.unlockmaster.data.repository.UnlockEventsRepositoryFake
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.CounterPausedEvent
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.CounterUnpausedEvent
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.LockEvent
import com.sweak.unlockmaster.domain.model.UnlockMasterEvent.UnlockEvent
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

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
    fun `There are no screen events in the given and previous days, given day is the current day and it is 4 30 AM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678764600000

        Assert.assertEquals(
            listOf(60, 60, 60, 60, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1678764600000)
        )
    }

    @Test
    fun `There are no screen events in the given and previous days and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678764600000

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1678706400000)
        )
    }

    @Test
    fun `UnlockEvent is the latest event from previous day, there are no given day events, given day is the current day and it is 4 30 AM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678764600000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1678746645000)
        )

        Assert.assertEquals(
            listOf(60, 60, 60, 60, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1678764600000)
        )
    }

    @Test
    fun `UnlockEvent is the latest event from previous day, there are no given day events and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678975800000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1678746645000)
        )

        Assert.assertEquals(
            listOf(60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60),
            getHourlyUsageMinutesForGivenDayUseCase(1678764600000)
        )
    }

    @Test
    fun `LockEvent is the latest event from previous day, there are no given day events, given day is the current day and it is 0 45 AM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678751100000

        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1678746645000)
        )

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1678751100000)
        )
    }

    @Test
    fun `LockEvent is the latest event from previous day, there are no given day events and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678975800000

        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1678746645000)
        )

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1678751100000)
        )
    }

    @Test
    fun `CounterPausedEvent is the latest event from previous day, there are no given day events and given day is the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678751100000

        counterPausedEventsRepository.counterPausedEventsSinceTimeToBeReturned = listOf(
            CounterPausedEvent(1678746645000)
        )

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1678751100000)
        )
    }

    @Test
    fun `CounterPausedEvent is the latest event from previous day, there are no given day events and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678975800000

        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1678746645000)
        )

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1678751100000)
        )
    }

    @Test
    fun `CounterUnpausedEvent is the latest event from previous day, there are no given day events, given day is the current day and it is 4 30 AM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678764600000

        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(1678746645000)
        )

        Assert.assertEquals(
            listOf(60, 60, 60, 60, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1678764600000)
        )
    }

    @Test
    fun `CounterUnpausedEvent is the latest event from previous day, there are no given day events and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678975800000

        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(1678746645000)
        )

        Assert.assertEquals(
            listOf(60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60),
            getHourlyUsageMinutesForGivenDayUseCase(1678764600000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one UnlockEvent in the given day, given day is the current day and it is 9 00 PM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676835000000)
        )

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1676836800000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one UnlockEvent in the given day and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678975800000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676835000000)
        )

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 60, 60, 60),
            getHourlyUsageMinutesForGivenDayUseCase(1676836800000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one LockEvent in the given day, given day is the current day and it is 3 00 AM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676772000000

        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676762400000)
        )

        Assert.assertEquals(
            listOf(20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1676772000000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one LockEvent in the given day and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678975800000

        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676762400000)
        )

        Assert.assertEquals(
            listOf(20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1676772000000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one CounterUnpausedEvent in the given day, given day is the current day and it is 9 00 PM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000

        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(1676832300000)
        )

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 60, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1676836800000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one CounterUnpausedEvent in the given day and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678975800000

        counterUnpausedEventsRepository.counterUnpausedEventsSinceTimeToBeReturned = listOf(
            CounterUnpausedEvent(1676832300000)
        )

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 60, 60, 60, 60),
            getHourlyUsageMinutesForGivenDayUseCase(1676836800000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one CounterPausedEvent in the given day, given day is the current day and it is 7 30 AM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676788200000

        counterPausedEventsRepository.counterPausedEventsSinceTimeToBeReturned = listOf(
            CounterPausedEvent(1676772900000)
        )

        Assert.assertEquals(
            listOf(60, 60, 60, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1676788200000)
        )
    }

    @Test
    fun `There are no events from previous day, there is only one CounterPausedEvent in the given day and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678975800000

        counterPausedEventsRepository.counterPausedEventsSinceTimeToBeReturned = listOf(
            CounterPausedEvent(1676772900000)
        )

        Assert.assertEquals(
            listOf(60, 60, 60, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1676788200000)
        )
    }

    @Test
    fun `There are no events from previous day, there are only three UnlockEvents in the given day, given day is the current day and it is 9 00 PM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676835000000),
            UnlockEvent(1676835600000),
            UnlockEvent(1676835900000)
        )

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1676836800000)
        )
    }

    @Test
    fun `There are no events from previous day, there are only three UnlockEvents in the given day and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678975800000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676835000000),
            UnlockEvent(1676835600000),
            UnlockEvent(1676835900000)
        )

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 60, 60, 60),
            getHourlyUsageMinutesForGivenDayUseCase(1676835900000)
        )
    }

    @Test
    fun `There are no events from previous day, there are only three LockEvents in the given day, given day is the current day and it is 6 45 AM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676785500000

        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676761800000),
            LockEvent(1676763000000),
            LockEvent(1676764800000)
        )

        Assert.assertEquals(
            listOf(10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1676785500000)
        )
    }

    @Test
    fun `There are no events from previous day, there are only three LockEvents in the given day and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678975800000

        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676761800000),
            LockEvent(1676763000000),
            LockEvent(1676764800000)
        )

        Assert.assertEquals(
            listOf(10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1676835900000)
        )
    }

    @Test
    fun `There are no events from previous day, there is specific sequence - ULULULUL in the given day, given day is the current day and it is 7 15 PM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676830500000

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
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 20, 0, 25, 5, 0, 10, 5, 0, 0, 10, 0, 0, 0, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1676830500000)
        )
    }

    @Test
    fun `LockEvent is the latest event from previous day, there is specific sequence - ULULULU in the given day, given day is the current day and it is 7 15 PM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676830500000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676790000000),
            UnlockEvent(1676799300000),
            UnlockEvent(1676811000000),
            UnlockEvent(1676824200000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676760300000),

            LockEvent(1676791200000),
            LockEvent(1676801100000),
            LockEvent(1676811900000),
        )

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 20, 0, 25, 5, 0, 10, 5, 0, 0, 30, 60, 15, 0, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1676830500000)
        )
    }

    @Test
    fun `LockEvent is the latest event from previous day, there is specific sequence - ULULULU in the given day and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678975800000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676790000000),
            UnlockEvent(1676799300000),
            UnlockEvent(1676811000000),
            UnlockEvent(1676843700000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676760300000),

            LockEvent(1676791200000),
            LockEvent(1676801100000),
            LockEvent(1676811900000),
        )

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 20, 0, 25, 5, 0, 10, 5, 0, 0, 0, 0, 0, 0, 0, 5, 60),
            getHourlyUsageMinutesForGivenDayUseCase(1676835900000)
        )
    }

    @Test
    fun `UnlockEvent is the latest event from previous day, there is specific sequence - LULULUL in the given day, given day is the current day and it is 7 15 PM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676830500000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676760300000),

            UnlockEvent(1676799300000),
            UnlockEvent(1676811000000),
            UnlockEvent(1676824200000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676762100000),
            LockEvent(1676801100000),
            LockEvent(1676811900000),
            LockEvent(1676824800000)
        )

        Assert.assertEquals(
            listOf(15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 5, 0, 10, 5, 0, 0, 10, 0, 0, 0, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1676830500000)
        )
    }

    @Test
    fun `UnlockEvent is the latest event from previous day, there is specific sequence - LULULUL in the given day and given day is not the current day`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1678975800000

        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676760300000),

            UnlockEvent(1676799300000),
            UnlockEvent(1676811000000),
            UnlockEvent(1676824200000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(1676762100000),
            LockEvent(1676801100000),
            LockEvent(1676811900000),
            LockEvent(1676824800000)
        )

        Assert.assertEquals(
            listOf(15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 25, 5, 0, 10, 5, 0, 0, 10, 0, 0, 0, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1676835900000)
        )
    }

    @Test
    fun `There are no events from previous day, there is specific sequence - U(CP)(CU)L in the given day, given day is the current day and it is 8 45 PM`() = runTest {
        timeRepository.currentTimeInMillisToBeReturned = 1676835900000

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

        Assert.assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0),
            getHourlyUsageMinutesForGivenDayUseCase(1676835900000)
        )
    }
}