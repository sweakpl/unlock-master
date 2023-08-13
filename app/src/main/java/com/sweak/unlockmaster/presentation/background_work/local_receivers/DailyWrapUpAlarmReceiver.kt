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
import com.sweak.unlockmaster.domain.repository.TimeRepository
import com.sweak.unlockmaster.presentation.MainActivity
import com.sweak.unlockmaster.presentation.background_work.DAILY_WRAP_UPS_NOTIFICATIONS_CHANNEL_ID
import com.sweak.unlockmaster.presentation.background_work.DAILY_WRAP_UP_NOTIFICATION_ID
import com.sweak.unlockmaster.presentation.background_work.DAILY_WRAP_UP_NOTIFICATION_REQUEST_CODE
import com.sweak.unlockmaster.presentation.background_work.EXTRA_DAILY_WRAP_UP_DAY_MILLIS
import com.sweak.unlockmaster.presentation.background_work.EXTRA_SHOW_DAILY_WRAP_UP_SCREEN
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DailyWrapUpAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    @Inject
    lateinit var timeRepository: TimeRepository

    override fun onReceive(context: Context, intent: Intent) {
        try {
            notificationManager.notify(
                DAILY_WRAP_UP_NOTIFICATION_ID,
                getDailyWrapUpNotification(context)
            )
        } catch (_: SecurityException) { /* no-op */ }
    }

    private fun getDailyWrapUpNotification(context: Context): Notification {
        val dailyWrapUpNotificationPendingIntent = PendingIntent.getActivity(
            context,
            DAILY_WRAP_UP_NOTIFICATION_REQUEST_CODE,
            Intent(context, MainActivity::class.java).apply {
                putExtra(EXTRA_SHOW_DAILY_WRAP_UP_SCREEN, true)
                putExtra(
                    EXTRA_DAILY_WRAP_UP_DAY_MILLIS,
                    timeRepository.getCurrentTimeInMillis()
                )
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
            setLights(Color.GREEN, 3000, 1000)
            build()
        }
    }
}