package com.sweak.unlockmaster

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.media.AudioAttributes
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import com.sweak.unlockmaster.presentation.unlock_counting.FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID
import com.sweak.unlockmaster.presentation.unlock_counting.MOBILIZING_NOTIFICATION_CHANNEL_ID
import com.sweak.unlockmaster.presentation.unlock_counting.UnlockMasterService
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
class UnlockMasterApplication : Application() {

    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    @Inject
    lateinit var userSessionRepository: UserSessionRepository

    override fun onCreate() {
        super.onCreate()

        createNotificationChannelsIfVersionRequires()
        startUnlockMasterServiceIfUserHasFinishedIntroduction()
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

            notificationManager.apply {
                createNotificationChannel(foregroundServiceNotificationChannel)
                createNotificationChannel(mobilizingNotificationsChannel)
            }
        }
    }

    private fun startUnlockMasterServiceIfUserHasFinishedIntroduction() {
        if (runBlocking { userSessionRepository.isIntroductionFinished() }) {
            val serviceIntent = Intent(this, UnlockMasterService::class.java)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent)
            } else {
                startService(serviceIntent)
            }
        }
    }
}