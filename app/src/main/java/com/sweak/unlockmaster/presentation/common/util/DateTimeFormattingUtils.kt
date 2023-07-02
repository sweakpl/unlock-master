package com.sweak.unlockmaster.presentation.common.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun getShortDayString(timeInMillis: Long): String =
    DateTimeFormatter
        .ofPattern("EEE", Locale.getDefault())
        .format(Instant.ofEpochMilli(timeInMillis).atZone(ZoneId.systemDefault()))

fun getTimeString(timeInMillis: Long, timeFormat: TimeFormat): String =
    DateTimeFormatter
        .ofPattern(
            if (timeFormat == TimeFormat.MILITARY) "HH:mm" else "hh:mm a",
            Locale.getDefault()
        )
        .format(Instant.ofEpochMilli(timeInMillis).atZone(ZoneId.systemDefault()))

fun getFullDateString(timeInMillis: Long): String =
    DateTimeFormatter
        .ofPattern("d MMM yyyy, EEEE", Locale.getDefault())
        .format(Instant.ofEpochMilli(timeInMillis).atZone(ZoneId.systemDefault()))

enum class TimeFormat {
    MILITARY, AMPM
}