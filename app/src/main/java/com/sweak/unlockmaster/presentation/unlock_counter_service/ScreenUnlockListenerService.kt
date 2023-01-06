package com.sweak.unlockmaster.presentation.unlock_counter_service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.sweak.unlockmaster.*
import com.sweak.unlockmaster.presentation.MainActivity

class ScreenUnlockListenerService : Service() {

    private var unlockCount = 0

    private val screenUnlockReceiver = ScreenUnlockReceiver().apply {
        onScreenUnlock = {
            unlockCount += 1

            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).notify(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) FOREGROUND_SERVICE_ID
                else FOREGROUND_SERVICE_NOTIFICATION_ID,
                createNewServiceNotification()
            )
        }
    }

    override fun onCreate() {
        super.onCreate()

        registerReceiver(screenUnlockReceiver, IntentFilter(Intent.ACTION_USER_PRESENT))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                startForeground(
                    FOREGROUND_SERVICE_ID,
                    createNewServiceNotification(),
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                startForeground(
                    FOREGROUND_SERVICE_ID,
                    createNewServiceNotification()
                )
            }
            else -> {
                (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).notify(
                    FOREGROUND_SERVICE_NOTIFICATION_ID,
                    createNewServiceNotification()
                )
            }
        }

        return START_STICKY
    }

    private fun createNewServiceNotification(): Notification {
        val notificationPendingIntent = PendingIntent.getActivity(
            applicationContext,
            FOREGROUND_SERVICE_NOTIFICATION_REQUEST_CODE,
            Intent(applicationContext, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        PendingIntent.FLAG_IMMUTABLE
                    else 0
        )

        NotificationCompat.Builder(
            applicationContext,
            FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID
        ).apply {
            priority = NotificationCompat.PRIORITY_LOW
            setOngoing(true)
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(getString(R.string.app_name))
            setContentText(getString(R.string.unlock_count_is, unlockCount))
            setContentIntent(notificationPendingIntent)
            return build()
        }
    }

    override fun onDestroy() {
        unregisterReceiver(screenUnlockReceiver)

        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}