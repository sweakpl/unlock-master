package com.sweak.unlockmaster.presentation.common.util

import java.text.SimpleDateFormat
import java.util.*

fun getShortDayString(timeInMillis: Long): String {
    return SimpleDateFormat(
        "EEE",
        Locale.getDefault()
    ).format(timeInMillis)
}