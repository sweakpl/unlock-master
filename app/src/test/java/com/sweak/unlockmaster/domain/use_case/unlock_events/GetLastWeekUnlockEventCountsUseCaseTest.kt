package com.sweak.unlockmaster.domain.use_case.unlock_events

import com.sweak.unlockmaster.data.repository.TimeRepositoryFake
import com.sweak.unlockmaster.data.repository.UnlockEventsRepositoryFake
import com.sweak.unlockmaster.domain.model.UnlockEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetLastWeekUnlockEventCountsUseCaseTest {

    private lateinit var getLastWeekUnlockEventCountsUseCase: GetLastWeekUnlockEventCountsUseCase
    private lateinit var unlockEventsRepository: UnlockEventsRepositoryFake
    private lateinit var timeRepository: TimeRepositoryFake

    @Before
    fun setUp() {
        unlockEventsRepository = UnlockEventsRepositoryFake()
        timeRepository = TimeRepositoryFake()
        getLastWeekUnlockEventCountsUseCase = GetLastWeekUnlockEventCountsUseCase(
            unlockEventsRepository,
            timeRepository
        )
    }

    @Test
    fun `If there are no UnlockEvents, then returns 0, 0, 0, 0, 0, 0, 0`() = runTest {
        timeRepository.tomorrowBeginningTimeInMillisToBeReturned = 1677452400000
        timeRepository.sixDaysBeforeDayBeginningTimeInMillisToBeReturned = 1676847600000

        getLastWeekUnlockEventCountsUseCase().apply {
            Assert.assertEquals(listOf(0, 0, 0, 0, 0, 0, 0), this)
        }
    }

    @Test
    fun `If there are UnlockEvents at the first second of each day, then returns 1, 1, 1, 1, 1, 1, 1`() = runTest {
        timeRepository.tomorrowBeginningTimeInMillisToBeReturned = 1677452400000
        timeRepository.sixDaysBeforeDayBeginningTimeInMillisToBeReturned = 1676847600000
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676847600000),
            UnlockEvent(1676934000000),
            UnlockEvent(1677020400000),
            UnlockEvent(1677106800000),
            UnlockEvent(1677193200000),
            UnlockEvent(1677279600000),
            UnlockEvent(1677366000000),
        )

        getLastWeekUnlockEventCountsUseCase().apply {
            Assert.assertEquals(listOf(1, 1, 1, 1, 1, 1, 1), this)
        }
    }

    @Test
    fun `If there are UnlockEvents at the last second of each day, then returns 1, 1, 1, 1, 1, 1, 1`() = runTest {
        timeRepository.tomorrowBeginningTimeInMillisToBeReturned = 1677452400000
        timeRepository.sixDaysBeforeDayBeginningTimeInMillisToBeReturned = 1676847600000
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676933999000),
            UnlockEvent(1677020399000),
            UnlockEvent(1677106799000),
            UnlockEvent(1677193199000),
            UnlockEvent(1677279599000),
            UnlockEvent(1677365999000),
            UnlockEvent(1677452399000),
        )

        getLastWeekUnlockEventCountsUseCase().apply {
            Assert.assertEquals(listOf(1, 1, 1, 1, 1, 1, 1), this)
        }
    }

    @Test
    fun `If there are specific UnlockEvents sequence, then returns 2, 4, 0, 0, 0, 0, 0`() = runTest {
        timeRepository.tomorrowBeginningTimeInMillisToBeReturned = 1677452400000
        timeRepository.sixDaysBeforeDayBeginningTimeInMillisToBeReturned = 1676847600000
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1676887200000),
            UnlockEvent(1676914200000),
            UnlockEvent(1676957400000),
            UnlockEvent(1676978100000),
            UnlockEvent(1676988000000),
            UnlockEvent(1677001500000)
        )

        getLastWeekUnlockEventCountsUseCase().apply {
            Assert.assertEquals(listOf(2, 4, 0, 0, 0, 0, 0), this)
        }
    }

    @Test
    fun `If there are specific UnlockEvents sequence, then returns 3, 5, 2, 2, 0, 0, 0`() = runTest {
        timeRepository.tomorrowBeginningTimeInMillisToBeReturned = 1642978800000
        timeRepository.sixDaysBeforeDayBeginningTimeInMillisToBeReturned = 1642374000000
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1642383599000),
            UnlockEvent(1642387199000),
            UnlockEvent(1642390799000),
            UnlockEvent(1642477199000),
            UnlockEvent(1642480799000),
            UnlockEvent(1642484399000),
            UnlockEvent(1642487999000),
            UnlockEvent(1642491599000),
            UnlockEvent(1642577999000),
            UnlockEvent(1642581599000),
            UnlockEvent(1642667999000),
            UnlockEvent(1642671599000)
        )

        getLastWeekUnlockEventCountsUseCase().apply {
            Assert.assertEquals(listOf(3, 5, 2, 2, 0, 0, 0), this)
        }
    }

    @Test
    fun `If there are specific UnlockEvents sequence, then returns 1, 2, 9, 1, 6, 14, 8`() = runTest {
        timeRepository.tomorrowBeginningTimeInMillisToBeReturned = 1677538800000
        timeRepository.sixDaysBeforeDayBeginningTimeInMillisToBeReturned = 1677020400000
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1677063983000),
            UnlockEvent(1677180463944),
            UnlockEvent(1677180670524),
            UnlockEvent(1677231827021),
            UnlockEvent(1677233646823),
            UnlockEvent(1677237297360),
            UnlockEvent(1677239366872),
            UnlockEvent(1677241757595),
            UnlockEvent(1677243498748),
            UnlockEvent(1677245071604),
            UnlockEvent(1677246167283),
            UnlockEvent(1677247851849),
            UnlockEvent(1677322175102),
            UnlockEvent(1677431095518),
            UnlockEvent(1677436064985),
            UnlockEvent(1677438353005),
            UnlockEvent(1677438392068),
            UnlockEvent(1677438454091),
            UnlockEvent(1677438497139),
            UnlockEvent(1677483905829),
            UnlockEvent(1677485791668),
            UnlockEvent(1677488799929),
            UnlockEvent(1677488994487),
            UnlockEvent(1677493163684),
            UnlockEvent(1677495431129),
            UnlockEvent(1677498660606),
            UnlockEvent(1677502225540),
            UnlockEvent(1677503185652),
            UnlockEvent(1677503862959),
            UnlockEvent(1677504667585),
            UnlockEvent(1677506178672),
            UnlockEvent(1677506788517),
            UnlockEvent(1677509641754),
            UnlockEvent(1677580527878),
            UnlockEvent(1677584222702),
            UnlockEvent(1677584829618),
            UnlockEvent(1677586248462),
            UnlockEvent(1677590096315),
            UnlockEvent(1677593560626),
            UnlockEvent(1677620245933),
            UnlockEvent(1677623680353)
        )

        getLastWeekUnlockEventCountsUseCase().apply {
            Assert.assertEquals(listOf(1, 2, 9, 1, 6, 14, 8), this)
        }
    }

    @Test
    fun `If there are specific UnlockEvents sequence, then returns 0, 0, 22, 28, 29, 26, 19`() = runTest {
        timeRepository.tomorrowBeginningTimeInMillisToBeReturned = 1677538800000
        timeRepository.sixDaysBeforeDayBeginningTimeInMillisToBeReturned = 1677020400000
        unlockEventsRepository.unlockEventsSinceTimeToBeReturned = listOf(
            UnlockEvent(1677222647231),
            UnlockEvent(1677226525087),
            UnlockEvent(1677233964276),
            UnlockEvent(1677236194918),
            UnlockEvent(1677245570795),
            UnlockEvent(1677248351979),
            UnlockEvent(1677248824169),
            UnlockEvent(1677249342890),
            UnlockEvent(1677250860438),
            UnlockEvent(1677251987581),
            UnlockEvent(1677252863338),
            UnlockEvent(1677258227557),
            UnlockEvent(1677260587476),
            UnlockEvent(1677261397633),
            UnlockEvent(1677263212915),
            UnlockEvent(1677267435312),
            UnlockEvent(1677268111293),
            UnlockEvent(1677271566812),
            UnlockEvent(1677273474979),
            UnlockEvent(1677274290847),
            UnlockEvent(1677275177406),
            UnlockEvent(1677275534885),
            UnlockEvent(1677280868800),
            UnlockEvent(1677283283924),
            UnlockEvent(1677311739446),
            UnlockEvent(1677313942466),
            UnlockEvent(1677315966157),
            UnlockEvent(1677316466817),
            UnlockEvent(1677319480477),
            UnlockEvent(1677320643069),
            UnlockEvent(1677322060783),
            UnlockEvent(1677323983534),
            UnlockEvent(1677326032037),
            UnlockEvent(1677329701340),
            UnlockEvent(1677341083366),
            UnlockEvent(1677341176381),
            UnlockEvent(1677345051433),
            UnlockEvent(1677345268674),
            UnlockEvent(1677345692869),
            UnlockEvent(1677345986590),
            UnlockEvent(1677346799461),
            UnlockEvent(1677347121337),
            UnlockEvent(1677348620488),
            UnlockEvent(1677348745002),
            UnlockEvent(1677350112750),
            UnlockEvent(1677356516433),
            UnlockEvent(1677357846314),
            UnlockEvent(1677362378517),
            UnlockEvent(1677362876880),
            UnlockEvent(1677364546422),
            UnlockEvent(1677369363173),
            UnlockEvent(1677370705290),
            UnlockEvent(1677400232235),
            UnlockEvent(1677401183126),
            UnlockEvent(1677402980210),
            UnlockEvent(1677403872758),
            UnlockEvent(1677404677055),
            UnlockEvent(1677405503442),
            UnlockEvent(1677406607869),
            UnlockEvent(1677406908744),
            UnlockEvent(1677408093814),
            UnlockEvent(1677410555671),
            UnlockEvent(1677414516791),
            UnlockEvent(1677418319459),
            UnlockEvent(1677422245584),
            UnlockEvent(1677424846494),
            UnlockEvent(1677430458739),
            UnlockEvent(1677434204407),
            UnlockEvent(1677436111754),
            UnlockEvent(1677438734500),
            UnlockEvent(1677438817570),
            UnlockEvent(1677439864285),
            UnlockEvent(1677440172211),
            UnlockEvent(1677440750609),
            UnlockEvent(1677441414823),
            UnlockEvent(1677441866122),
            UnlockEvent(1677441952324),
            UnlockEvent(1677443487103),
            UnlockEvent(1677445913133),
            UnlockEvent(1677474916831),
            UnlockEvent(1677475931878),
            UnlockEvent(1677477315890),
            UnlockEvent(1677479051778),
            UnlockEvent(1677479203417),
            UnlockEvent(1677480592129),
            UnlockEvent(1677484181460),
            UnlockEvent(1677489242441),
            UnlockEvent(1677494105857),
            UnlockEvent(1677497115131),
            UnlockEvent(1677502040948),
            UnlockEvent(1677507036241),
            UnlockEvent(1677507421771),
            UnlockEvent(1677507730451),
            UnlockEvent(1677510158167),
            UnlockEvent(1677514391936),
            UnlockEvent(1677516119014),
            UnlockEvent(1677516506855),
            UnlockEvent(1677517991518),
            UnlockEvent(1677521144866),
            UnlockEvent(1677522918543),
            UnlockEvent(1677525700236),
            UnlockEvent(1677526386987),
            UnlockEvent(1677526516517),
            UnlockEvent(1677526540999),
            UnlockEvent(1677536260481),
            UnlockEvent(1677541938094),
            UnlockEvent(1677570987005),
            UnlockEvent(1677576555834),
            UnlockEvent(1677576695671),
            UnlockEvent(1677576916273),
            UnlockEvent(1677579213880),
            UnlockEvent(1677583271436),
            UnlockEvent(1677589693052),
            UnlockEvent(1677595812579),
            UnlockEvent(1677596736837),
            UnlockEvent(1677598291714),
            UnlockEvent(1677599305998),
            UnlockEvent(1677599664751),
            UnlockEvent(1677613541725),
            UnlockEvent(1677616481370),
            UnlockEvent(1677619373790),
            UnlockEvent(1677619649549),
            UnlockEvent(1677620398521),
            UnlockEvent(1677623956144)
        )

        getLastWeekUnlockEventCountsUseCase().apply {
            Assert.assertEquals(listOf(0, 0, 22, 28, 29, 26, 19), this)
        }
    }
}