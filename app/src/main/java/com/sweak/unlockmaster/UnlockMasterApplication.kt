package com.sweak.unlockmaster

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import com.sweak.unlockmaster.presentation.unlock_counter_service.FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID
import com.sweak.unlockmaster.presentation.unlock_counter_service.UnlockMasterService
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

        createNotificationChannelIfVersionRequires()
        startUnlockMasterServiceIfUserHasFinishedIntroduction()
    }

    private fun createNotificationChannelIfVersionRequires() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val alarmNotificationChannel = NotificationChannel(
                FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID,
                getString(R.string.background_service_notification_channel_title),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description =
                    getString(R.string.background_service_notification_channel_description)
            }

            notificationManager.createNotificationChannel(alarmNotificationChannel)
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