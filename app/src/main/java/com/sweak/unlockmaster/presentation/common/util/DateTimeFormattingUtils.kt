package com.sweak.unlockmaster.presentation.common.util

import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.*

fun ZonedDateTime.toTimeInMillis(): Long =
    this.toInstant().toEpochMilli()

fun getShortDayString(timeInMillis: Long): String {
    return SimpleDateFormat(
        "EEE",
        Locale.getDefault()
    ).format(timeInMillis)
}