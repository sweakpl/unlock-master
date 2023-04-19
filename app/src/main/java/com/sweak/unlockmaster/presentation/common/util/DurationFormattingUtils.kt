package com.sweak.unlockmaster.presentation.common.util

fun getHoursAndMinutesDurationPair(durationTimeInMillis: Long): Pair<Int, Int> {
    val hours = durationTimeInMillis / 3600000
    val minutes = (durationTimeInMillis % 3600000) / 60000

    return Pair(hours.toInt(), minutes.toInt())
}

fun getHoursMinutesAndSecondsDurationTriple(durationTimeInMillis: Long):
        Triple<Int, Int, Int> {
    val hours = durationTimeInMillis / 3600000
    val minutes = (durationTimeInMillis % 3600000) / 60000
    val seconds = (durationTimeInMillis % 60000) / 1000

    return Triple(hours.toInt(), minutes.toInt(), seconds.toInt())
}