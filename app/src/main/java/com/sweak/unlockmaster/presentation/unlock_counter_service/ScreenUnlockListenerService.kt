package com.sweak.unlockmaster.presentation.unlock_counter_service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.domain.use_case.lock_events.AddLockEventUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_events.AddUnlockEventUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_events.GetTodayUnlockEventsCountUseCase
import com.sweak.unlockmaster.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class ScreenUnlockListenerService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO)

    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    @Inject
    lateinit var addUnlockEventUseCase: AddUnlockEventUseCase

    @Inject
    lateinit var addLockEventUseCase: AddLockEventUseCase

    @Inject
    lateinit var getTodayUnlockEventsCountUseCase: GetTodayUnlockEventsCountUseCase

    private val screenUnlockReceiver = ScreenUnlockReceiver().apply {
        onScreenUnlock = {
            serviceScope.launch {
                addUnlockEventUseCase()

                try {
                    notificationManager.notify(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) FOREGROUND_SERVICE_ID
                        else FOREGROUND_SERVICE_NOTIFICATION_ID,
                        createNewServiceNotification(getTodayUnlockEventsCountUseCase())
                    )
                } catch (_: SecurityException) { /* no-op */ }
            }
        }
    }

    private val screenLockReceiver = ScreenLockReceiver().apply {
        onScreenLock = {
            serviceScope.launch {
                addLockEventUseCase()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        registerReceiver(screenUnlockReceiver, IntentFilter(Intent.ACTION_USER_PRESENT))
        registerReceiver(screenLockReceiver, IntentFilter(Intent.ACTION_SCREEN_OFF))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceScope.launch {
            val todayUnlockEventsCount = getTodayUnlockEventsCountUseCase()

            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                    startForeground(
                        FOREGROUND_SERVICE_ID,
                        createNewServiceNotification(todayUnlockEventsCount),
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST
                    )
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    startForeground(
                        FOREGROUND_SERVICE_ID,
                        createNewServiceNotification(todayUnlockEventsCount)
                    )
                }
                else -> {
                    try {
                        notificationManager.notify(
                            FOREGROUND_SERVICE_NOTIFICATION_ID,
                            createNewServiceNotification(todayUnlockEventsCount)
                        )
                    } catch (_: SecurityException) { /* no-op */ }
                }
            }
        }

        return START_STICKY
    }

    private fun createNewServiceNotification(todayUnlockEventsCount: Int): Notification {
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
            setSmallIcon(R.drawable.ic_notification_icon)
            setContentTitle(getString(R.string.app_name))
            setContentText(getString(R.string.unlock_count_is, todayUnlockEventsCount))
            setContentIntent(notificationPendingIntent)
            return build()
        }
    }

    override fun onDestroy() {
        unregisterReceiver(screenUnlockReceiver)
        unregisterReceiver(screenLockReceiver)

        serviceScope.cancel(
            CancellationException("ScreenUnlockListenerService has been destroyed")
        )

        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? = null
}