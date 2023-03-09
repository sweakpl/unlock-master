package com.sweak.unlockmaster.presentation.common.util

import java.text.SimpleDateFormat
import java.util.*

fun getShortDayString(timeInMillis: Long): String {
    return SimpleDateFormat(
        "EEE",
        Locale.getDefault()
    ).format(timeInMillis)
}

fun getTimeString(timeInMillis: Long, timeFormat: TimeFormat): String {
    return SimpleDateFormat(
        if (timeFormat == TimeFormat.MILITARY) "HH:mm" else "hh:mm a",
        Locale.getDefault()
    ).format(timeInMillis)
}

enum class TimeFormat {
    MILITARY, AMPM
}