package com.sweak.unlockmaster.domain

import java.time.ZonedDateTime

fun ZonedDateTime.toTimeInMillis(): Long =
    this.toInstant().toEpochMilli()