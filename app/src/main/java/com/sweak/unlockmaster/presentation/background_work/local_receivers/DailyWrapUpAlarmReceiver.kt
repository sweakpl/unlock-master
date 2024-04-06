package com.sweak.unlockmaster.presentation.background_work.local_receivers

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.domain.MINIMAL_DAILY_WRAP_UPS_NOTIFICATIONS_TIME_HOUR_OF_DAY
import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.domain.toTimeInMillis
import com.sweak.unlockmaster.domain.use_case.daily_wrap_up.GetDailyWrapUpNotificationsTimeUseCase
import com.sweak.unlockmaster.domain.use_case.daily_wrap_up.ScheduleDailyWrapUpNotificationsUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_events.GetUnlockEventsCountForGivenDayUseCase
import com.sweak.unlockmaster.presentation.MainActivity
import com.sweak.unlockmaster.presentation.background_work.DAILY_WRAP_UPS_NOTIFICATIONS_CHANNEL_ID
import com.sweak.unlockmaster.presentation.background_work.DAILY_WRAP_UP_NOTIFICATION_ID
import com.sweak.unlockmaster.presentation.background_work.DAILY_WRAP_UP_NOTIFICATION_REQUEST_CODE
import com.sweak.unlockmaster.presentation.background_work.EXTRA_DAILY_WRAP_UP_DAY_MILLIS
import com.sweak.unlockmaster.presentation.background_work.EXTRA_SHOW_DAILY_WRAP_UP_SCREEN
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

@AndroidEntryPoint
class DailyWrapUpAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    @Inject
    lateinit var timeRepository: TimeRepository

    @Inject
    lateinit var getUnlockEventsCountForGivenDayUseCase: GetUnlockEventsCountForGivenDayUseCase

    @Inject
    lateinit var getDailyWrapUpNotificationsTimeUseCase: GetDailyWrapUpNotificationsTimeUseCase

    @Inject
    lateinit var scheduleDailyWrapUpNotificationsUseCase: ScheduleDailyWrapUpNotificationsUseCase

    override fun onReceive(context: Context, intent: Intent) {
        try {
            getDailyWrapUpNotification(context)?.let {
                notificationManager.notify(DAILY_WRAP_UP_NOTIFICATION_ID, it)
            }
        } catch (_: SecurityException) { /* no-op */ }

        handlePotentialDaylightSavingTimeChange()
    }

    private fun getDailyWrapUpNotification(context: Context): Notification? {
        val dailyWrapUpDateTime = ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(timeRepository.getCurrentTimeInMillis()),
            ZoneId.systemDefault()
        )

        // We have to handle the case when the notification was e.g. scheduled for 23:50 and it was
        // delayed until 0:05 - in this case we have to decrease the current time's day so that the
        // time in millis passed as an intent extra is correctly interpreted in the daily wrap-up:
        val dailyWrapUpDayTimeInMillis =
            if (dailyWrapUpDateTime.hour < MINIMAL_DAILY_WRAP_UPS_NOTIFICATIONS_TIME_HOUR_OF_DAY) {
                dailyWrapUpDateTime.minusDays(1).toTimeInMillis()
            } else {
                dailyWrapUpDateTime.toTimeInMillis()
            }

        val isDayEligibleForDailyWrapUp = runBlocking {
            getUnlockEventsCountForGivenDayUseCase(dailyWrapUpDayTimeInMillis) > 0
        }

        if (!isDayEligibleForDailyWrapUp) {
            return null
        }

        val dailyWrapUpNotificationPendingIntent = PendingIntent.getActivity(
            context,
            DAILY_WRAP_UP_NOTIFICATION_REQUEST_CODE,
            Intent(context, MainActivity::class.java).apply {
                putExtra(EXTRA_SHOW_DAILY_WRAP_UP_SCREEN, true)
                putExtra(EXTRA_DAILY_WRAP_UP_DAY_MILLIS, dailyWrapUpDayTimeInMillis)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        PendingIntent.FLAG_IMMUTABLE
                    else 0
        )

        return NotificationCompat.Builder(
            context,
            DAILY_WRAP_UPS_NOTIFICATIONS_CHANNEL_ID
        ).run {
            priority = NotificationCompat.PRIORITY_HIGH
            setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            setSmallIcon(R.drawable.ic_notification_icon)
            setContentTitle(context.getString(R.string.daily_wrapup))
            setContentText(context.getString(R.string.click_to_check_progress))
            setContentIntent(dailyWrapUpNotificationPendingIntent)
            setAutoCancel(true)
            setOngoing(true)
            setLights(Color.GREEN, 2000, 1000)
            build()
        }
    }

    private fun handlePotentialDaylightSavingTimeChange() = runBlocking {
        val (notificationHourOfDay, notificationMinute) =
            getDailyWrapUpNotificationsTimeUseCase().run { Pair(hourOfDay, minute) }
        val (currentHourOfDay, currentMinuteOfDay) = ZonedDateTime.now().run { Pair(hour, minute) }

        // We will compare the minutes difference between the current time and notification time:
        val notificationMinutesSum = 60 * notificationHourOfDay + notificationMinute
        val currentMinutesSum = 60 * currentHourOfDay + currentMinuteOfDay +
            // There might be a situation when the notification has been displayed the next day:
            if (currentHourOfDay < MINIMAL_DAILY_WRAP_UPS_NOTIFICATIONS_TIME_HOUR_OF_DAY)
                24 * 60 // minutes in a full day
            else 0

        val minutesDifference = currentMinutesSum - notificationMinutesSum

        // Example situations that can be handled:
        // Notification: 22:15, Current: 22:23 -> Diff = 8 - NO RESCHEDULE
        // Notification: 22:15, Current: 21:23 -> Diff = -52 - RESCHEDULE (time shifted back)
        // Notification: 22:15, Current: 23:23 -> Diff = 68 - RESCHEDULE (likely time forwarded)
        if (minutesDifference < 0 || minutesDifference >= 60) {
            scheduleDailyWrapUpNotificationsUseCase()
        }
    }
}