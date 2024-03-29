package com.sweak.unlockmaster.presentation.background_work

const val FOREGROUND_SERVICE_ID = 300
const val FOREGROUND_SERVICE_NOTIFICATION_ID = 300
const val FOREGROUND_SERVICE_NOTIFICATION_REQUEST_CODE = 400
const val FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID =
    "UnlockMasterForegroundServiceNotificationChannelId"

const val MOBILIZING_NOTIFICATION_ID = 500
const val MOBILIZING_NOTIFICATION_REQUEST_CODE = 600
const val MOBILIZING_NOTIFICATION_CHANNEL_ID = "UnlockMasterMobilizingNotificationChannelId"

const val DAILY_WRAP_UP_NOTIFICATION_REQUEST_CODE = 700
const val DAILY_WRAP_UP_NOTIFICATION_ID = 800
const val DAILY_WRAP_UPS_NOTIFICATIONS_CHANNEL_ID = "dailyWrapUpNotificationChannelId"

const val ACTION_UNLOCK_COUNTER_PAUSE_CHANGED =
    "com.sweak.unlockmaster.UNLOCK_COUNTER_PAUSE_CHANGED"
const val EXTRA_IS_UNLOCK_COUNTER_PAUSED = "com.sweak.unlockmaster.EXTRA_IS_UNLOCK_COUNTER_PAUSED"

const val EXTRA_SHOW_DAILY_WRAP_UP_SCREEN = "com.sweak.unlockmaster.EXTRA_SHOW_DAILY_WRAP_UP_SCREEN"
const val EXTRA_DAILY_WRAP_UP_DAY_MILLIS = "com.sweak.unlockmaster.EXTRA_DAILY_WRAP_UP_DAY_MILLIS"