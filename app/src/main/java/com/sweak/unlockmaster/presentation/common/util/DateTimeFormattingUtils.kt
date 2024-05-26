package com.sweak.unlockmaster.presentation.common.util

import android.content.Context
import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.util.Duration.*
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

@Composable
fun getCompactDurationString(duration: Duration): String {
    var (hours, minutes, seconds) = getHoursMinutesAndSecondsDurationTriple(duration.durationMillis)

    when (duration.precision) {
        DisplayPrecision.HOURS -> {
            seconds = 0
            minutes = 0
        }
        DisplayPrecision.MINUTES -> {
            seconds = 0
        }
        DisplayPrecision.SECONDS -> { /* no-op */ }
    }

    return StringBuilder("").run {
        if (duration.precision == DisplayPrecision.HOURS) {
            append(stringResource(R.string.hours_amount, hours))
            return@run toString()
        } else if (hours != 0) {
            append(stringResource(R.string.hours_amount, hours))
        }

        if (duration.precision == DisplayPrecision.MINUTES) {
            if (isBlank()) {
                append(stringResource(R.string.minutes_amount, minutes))
            } else if (minutes != 0) {
                append(" " + stringResource(R.string.minutes_amount, minutes))
            }
            return@run toString()
        } else {
            if (isBlank()) {
                if (minutes != 0) {
                    append(stringResource(R.string.minutes_amount, minutes))
                }
            } else {
                if (minutes != 0) {
                    append(" " + stringResource(R.string.minutes_amount, minutes))
                } else if (seconds != 0) {
                    append(" " + stringResource(R.string.minutes_amount, minutes))
                }
            }
        }

        if (isBlank()) {
            append(stringResource(R.string.seconds_amount, seconds))
        } else if (seconds != 0) {
            append(" " + stringResource(R.string.seconds_amount, seconds))
        }

        toString()
    }
}

fun getCompactDurationString(duration: Duration, resources: Resources): String {
    var (hours, minutes, seconds) = getHoursMinutesAndSecondsDurationTriple(duration.durationMillis)

    when (duration.precision) {
        DisplayPrecision.HOURS -> {
            seconds = 0
            minutes = 0
        }
        DisplayPrecision.MINUTES -> {
            seconds = 0
        }
        DisplayPrecision.SECONDS -> { /* no-op */ }
    }

    return StringBuilder("").run {
        if (duration.precision == DisplayPrecision.HOURS) {
            append(resources.getString(R.string.hours_amount, hours))
            return@run toString()
        } else if (hours != 0) {
            append(resources.getString(R.string.hours_amount, hours))
        }

        if (duration.precision == DisplayPrecision.MINUTES) {
            if (isBlank()) {
                append(resources.getString(R.string.minutes_amount, minutes))
            } else if (minutes != 0) {
                append(" " + resources.getString(R.string.minutes_amount, minutes))
            }
            return@run toString()
        } else {
            if (isBlank()) {
                if (minutes != 0) {
                    append(resources.getString(R.string.minutes_amount, minutes))
                }
            } else {
                if (minutes != 0) {
                    append(" " + resources.getString(R.string.minutes_amount, minutes))
                } else if (seconds != 0) {
                    append(" " + resources.getString(R.string.minutes_amount, minutes))
                }
            }
        }

        if (isBlank()) {
            append(resources.getString(R.string.seconds_amount, seconds))
        } else if (seconds != 0) {
            append(" " + resources.getString(R.string.seconds_amount, seconds))
        }

        toString()
    }
}

data class Duration(
    val durationMillis: Long,
    val precision: DisplayPrecision = DisplayPrecision.SECONDS
) {
    enum class DisplayPrecision {
        SECONDS, MINUTES, HOURS
    }
}

private fun getHoursMinutesAndSecondsDurationTriple(
    durationTimeInMillis: Long
): Triple<Int, Int, Int> {
    val hours = durationTimeInMillis / 3600000
    val minutes = (durationTimeInMillis % 3600000) / 60000
    val seconds = (durationTimeInMillis % 60000) / 1000

    return Triple(hours.toInt(), minutes.toInt(), seconds.toInt())
}