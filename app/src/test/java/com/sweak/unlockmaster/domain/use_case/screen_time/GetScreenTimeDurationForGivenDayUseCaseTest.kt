package com.sweak.unlockmaster.domain.use_case.screen_time

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetScreenTimeDurationForGivenDayUseCaseTest {

    private lateinit var getScreenTimeDurationForGivenDayUseCase: GetScreenTimeDurationForGivenDayUseCase

    @Before
    fun setUp() {
        getScreenTimeDurationForGivenDayUseCase = GetScreenTimeDurationForGivenDayUseCase()
    }

    @Test
    fun `test name`() = runTest {

    }
}