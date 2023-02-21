package com.sweak.unlockmaster.domain.use_case.screen_time

import com.sweak.unlockmaster.data.repository.LockEventsRepositoryFake
import com.sweak.unlockmaster.data.repository.TimeRepositoryFake
import com.sweak.unlockmaster.data.repository.UnlockEventsRepositoryFake
import com.sweak.unlockmaster.domain.model.LockEvent
import com.sweak.unlockmaster.domain.model.UnlockEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetTodayScreenTimeHoursAndMinutesUseCaseTest {

    private lateinit var getTodayScreenTimeHoursAndMinutesUseCase: GetTodayScreenTimeHoursAndMinutesUseCase
    private lateinit var unlockEventsRepository: UnlockEventsRepositoryFake
    private lateinit var lockEventsRepository: LockEventsRepositoryFake
    private lateinit var timeRepository: TimeRepositoryFake

    @Before
    fun setUp() {
        unlockEventsRepository = UnlockEventsRepositoryFake()
        lockEventsRepository = LockEventsRepositoryFake()
        timeRepository = TimeRepositoryFake()
        getTodayScreenTimeHoursAndMinutesUseCase = GetTodayScreenTimeHoursAndMinutesUseCase(
            unlockEventsRepository,
            lockEventsRepository,
            timeRepository
        )
    }

    @Test
    fun `If there are no screen events, then returns 0 hours and 0 minutes`() = runTest {
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = emptyList()
        lockEventsRepository.lockEventsSinceTimeToBeReturned = emptyList()
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        getTodayScreenTimeHoursAndMinutesUseCase().apply {
            Assert.assertEquals(0, first)
            Assert.assertEquals(0, second)
        }
    }

    @Test
    fun `If there is only one UnlockEvent, then returns 0 hours and 30 minutes`() = runTest {
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(unlockTimeInMillis = 1676835000000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = emptyList()
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        getTodayScreenTimeHoursAndMinutesUseCase().apply {
            Assert.assertEquals(0, first)
            Assert.assertEquals(30, second)
        }
    }

    @Test
    fun `If there is only one LockEvent, then returns 0 hours and 20 minutes`() = runTest {
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = emptyList()
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(lockTimeInMillis = 1676762400000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        getTodayScreenTimeHoursAndMinutesUseCase().apply {
            Assert.assertEquals(0, first)
            Assert.assertEquals(20, second)
        }
    }

    @Test
    fun `If there are only three UnlockEvents, then returns 0 hours and 15 minutes`() = runTest {
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(unlockTimeInMillis = 1676835000000),
            UnlockEvent(unlockTimeInMillis = 1676835600000),
            UnlockEvent(unlockTimeInMillis = 1676835900000)
        )
        lockEventsRepository.lockEventsSinceTimeToBeReturned = emptyList()
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        getTodayScreenTimeHoursAndMinutesUseCase().apply {
            Assert.assertEquals(0, first)
            Assert.assertEquals(15, second)
        }
    }

    @Test
    fun `If there are only three LockEvents, then returns 0 hours and 10 minutes`() = runTest {
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = emptyList()
        lockEventsRepository.lockEventsSinceTimeToBeReturned = listOf(
            LockEvent(lockTimeInMillis = 1676761800000),
            LockEvent(lockTimeInMillis = 1676763000000),
            LockEvent(lockTimeInMillis = 1676764800000)
        )
        timeRepository.currentTimeInMillisToBeReturned = 1676836800000
        timeRepository.todayBeginningTimeInMillisToBeReturned = 1676761200000

        getTodayScreenTimeHoursAndMinutesUseCase().apply {
            Assert.assertEquals(0, first)
            Assert.assertEquals(10, second)
        }
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

        getTodayScreenTimeHoursAndMinutesUseCase().apply {
            Assert.assertEquals(1, first)
            Assert.assertEquals(15, second)
        }
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

        getTodayScreenTimeHoursAndMinutesUseCase().apply {
            Assert.assertEquals(1, first)
            Assert.assertEquals(15, second)
        }
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

        getTodayScreenTimeHoursAndMinutesUseCase().apply {
            Assert.assertEquals(1, first)
            Assert.assertEquals(15, second)
        }
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

        getTodayScreenTimeHoursAndMinutesUseCase().apply {
            Assert.assertEquals(1, first)
            Assert.assertEquals(30, second)
        }
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

        getTodayScreenTimeHoursAndMinutesUseCase().apply {
            Assert.assertEquals(0, first)
            Assert.assertEquals(55, second)
        }
    }
}