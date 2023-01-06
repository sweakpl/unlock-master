package com.sweak.unlockmaster

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import com.sweak.unlockmaster.presentation.unlock_counter_service.FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID
import com.sweak.unlockmaster.presentation.unlock_counter_service.ScreenUnlockListenerService

class UnlockMasterApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        createNotificationChannelIfVersionRequires()
        startUnlockListenerService()
    }

    private fun createNotificationChannelIfVersionRequires() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val alarmNotificationChannel = NotificationChannel(
                FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID,
                getString(R.string.background_service_notification_channel_title),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.background_service_notification_channel_description)
            }

            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(alarmNotificationChannel)
        }
    }

    private fun startUnlockListenerService() {
        val serviceIntent = Intent(this, ScreenUnlockListenerService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }
}