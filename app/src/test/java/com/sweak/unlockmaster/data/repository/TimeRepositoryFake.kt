package com.sweak.unlockmaster.data.repository

import com.sweak.unlockmaster.domain.repository.TimeRepository

class TimeRepositoryFake : TimeRepository {

    var currentTimeInMillisToBeReturned: Long = 0
    var todayBeginningTimeInMillisToBeReturned: Long = 0

    override fun getCurrentTimeInMillis(): Long = currentTimeInMillisToBeReturned

    override fun getTodayBeginningTimeInMillis(): Long = todayBeginningTimeInMillisToBeReturned

    override fun getTomorrowBeginningTimeInMillis(): Long {
        TODO("Not yet implemented")
    }
}