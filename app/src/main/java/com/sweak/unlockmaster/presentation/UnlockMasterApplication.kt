package com.sweak.unlockmaster.presentation

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import com.sweak.unlockmaster.domain.use_case.daily_wrap_up.ScheduleDailyWrapUpNotificationsUseCase
import com.sweak.unlockmaster.presentation.background_work.DAILY_WRAP_UPS_NOTIFICATIONS_CHANNEL_ID
import com.sweak.unlockmaster.presentation.background_work.FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID
import com.sweak.unlockmaster.presentation.background_work.MOBILIZING_NOTIFICATION_CHANNEL_ID
import com.sweak.unlockmaster.presentation.background_work.UnlockMasterService
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
class UnlockMasterApplication : Application() {

    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    @Inject
    lateinit var userSessionRepository: UserSessionRepository

    @Inject
    lateinit var scheduleDailyWrapUpNotificationsUseCase: ScheduleDailyWrapUpNotificationsUseCase

    override fun onCreate() {
        super.onCreate()

        createNotificationChannelsIfVersionRequires()
        setUpUnlockMasterServiceAndDailyWrapUpsIfUserHasFinishedIntroduction()
    }

    private fun createNotificationChannelsIfVersionRequires() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val foregroundServiceNotificationChannel = NotificationChannel(
                FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID,
                getString(R.string.background_service_notification_channel_title),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description =
                    getString(R.string.background_service_notification_channel_description)
            }

            val mobilizingNotificationsChannel = NotificationChannel(
                MOBILIZING_NOTIFICATION_CHANNEL_ID,
                getString(R.string.mobilizing_notifications_channel_title),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setSound(
                    Settings.System.DEFAULT_NOTIFICATION_URI,
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                description = getString(R.string.mobilizing_notifications_channel_description)
            }

            val dailyWrapUpNotificationsChannel = NotificationChannel(
                DAILY_WRAP_UPS_NOTIFICATIONS_CHANNEL_ID,
                getString(R.string.daily_wrapup_notifications_channel_title),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setSound(
                    Settings.System.DEFAULT_NOTIFICATION_URI,
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                description = getString(R.string.daily_wrapup_notifications_channel_description)
                enableLights(true)
                lightColor = Color.GREEN
            }

            notificationManager.apply {
                createNotificationChannel(foregroundServiceNotificationChannel)
                createNotificationChannel(mobilizingNotificationsChannel)
                createNotificationChannel(dailyWrapUpNotificationsChannel)
            }
        }
    }

    private fun setUpUnlockMasterServiceAndDailyWrapUpsIfUserHasFinishedIntroduction() {
        runBlocking {
            if (userSessionRepository.isIntroductionFinished()) {
                val serviceIntent =
                    Intent(this@UnlockMasterApplication, UnlockMasterService::class.java)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent)
                } else {
                    startService(serviceIntent)
                }

                scheduleDailyWrapUpNotificationsUseCase()
            }
        }
    }
}