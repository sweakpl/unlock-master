package com.sweak.unlockmaster.presentation.unlock_counter_service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.domain.use_case.lock_events.AddLockEventUseCase
import com.sweak.unlockmaster.domain.use_case.lock_events.ShouldAddLockEventUseCase
import com.sweak.unlockmaster.domain.use_case.screen_on_events.AddScreenOnEventUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_events.AddUnlockEventUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_events.GetTodayUnlockEventsCountUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_limits.GetUnlockLimitAmountForTodayUseCase
import com.sweak.unlockmaster.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class UnlockMasterService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO)

    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    @Inject
    lateinit var addUnlockEventUseCase: AddUnlockEventUseCase

    @Inject
    lateinit var addLockEventUseCase: AddLockEventUseCase

    @Inject
    lateinit var shouldAddLockEventUseCase: ShouldAddLockEventUseCase

    @Inject
    lateinit var addScreenEventUseCase: AddScreenOnEventUseCase

    @Inject
    lateinit var getTodayUnlockEventsCountUseCase: GetTodayUnlockEventsCountUseCase

    @Inject
    lateinit var getUnlockLimitAmountForTodayUseCase: GetUnlockLimitAmountForTodayUseCase

    private val unlockCounterPauseReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (intent.action == UNLOCK_COUNTER_PAUSE_CHANGED) {
                    serviceScope.launch {
                        val isUnlockCounterPaused = intent.getBooleanExtra(
                            EXTRA_IS_UNLOCK_COUNTER_PAUSED,
                            false
                        )

                        if (isUnlockCounterPaused) {
                            unregisterScreenEventReceivers()

                            // We're adding a lock event for the data to be coherent as the service
                            // will not be able to catch the lock event that is supposed to be
                            // the one corresponding to the latest unlock event:
                            addLockEventUseCase()
                        } else {
                            registerScreenEventReceivers()
                        }

                        try {
                            notificationManager.notify(
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                                    FOREGROUND_SERVICE_ID
                                else FOREGROUND_SERVICE_NOTIFICATION_ID,
                                createNewServiceNotification(
                                    getTodayUnlockEventsCountUseCase(),
                                    getUnlockLimitAmountForTodayUseCase(),
                                    isUnlockCounterPaused
                                )
                            )
                        } catch (_: SecurityException) { /* no-op */ }
                    }
                }
            }
        }
    }

    private val screenUnlockReceiver = ScreenUnlockReceiver().apply {
        onScreenUnlock = {
            serviceScope.launch {
                addUnlockEventUseCase()

                try {
                    notificationManager.notify(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) FOREGROUND_SERVICE_ID
                        else FOREGROUND_SERVICE_NOTIFICATION_ID,
                        createNewServiceNotification(
                            getTodayUnlockEventsCountUseCase(),
                            getUnlockLimitAmountForTodayUseCase()
                        )
                    )
                } catch (_: SecurityException) { /* no-op */ }
            }
        }
    }

    private val screenLockReceiver = ScreenLockReceiver().apply {
        onScreenLock = {
            serviceScope.launch {
                if (shouldAddLockEventUseCase()) {
                    addLockEventUseCase()
                }
            }
        }
    }

    private val screenOnReceiver = ScreenOnReceiver().apply {
        onScreenOn = {
            serviceScope.launch {
                addScreenEventUseCase()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        registerScreenEventReceivers()
        registerReceiver(unlockCounterPauseReceiver, IntentFilter(UNLOCK_COUNTER_PAUSE_CHANGED))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceScope.launch {
            val todayUnlockEventsCount = getTodayUnlockEventsCountUseCase()
            val todayUnlockLimit = getUnlockLimitAmountForTodayUseCase()

            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                    startForeground(
                        FOREGROUND_SERVICE_ID,
                        createNewServiceNotification(todayUnlockEventsCount, todayUnlockLimit),
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST
                    )
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    startForeground(
                        FOREGROUND_SERVICE_ID,
                        createNewServiceNotification(todayUnlockEventsCount, todayUnlockLimit)
                    )
                }
                else -> {
                    try {
                        notificationManager.notify(
                            FOREGROUND_SERVICE_NOTIFICATION_ID,
                            createNewServiceNotification(todayUnlockEventsCount, todayUnlockLimit)
                        )
                    } catch (_: SecurityException) { /* no-op */ }
                }
            }
        }

        return START_STICKY
    }

    private fun createNewServiceNotification(
        todayUnlockEventsCount: Int,
        todayUnlockLimit: Int,
        isUnlockCounterPaused: Boolean = false
    ): Notification {
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
            setContentTitle(
                getString(
                    if (isUnlockCounterPaused) R.string.unlock_master_paused else R.string.app_name
                )
            )
            setContentText(
                getString(R.string.your_unlock_count_is, todayUnlockEventsCount, todayUnlockLimit)
            )
            setContentIntent(notificationPendingIntent)
            return build()
        }
    }

    override fun onDestroy() {
        unregisterScreenEventReceivers()
        unregisterReceiver(unlockCounterPauseReceiver)

        serviceScope.cancel(
            CancellationException("UnlockMasterService has been destroyed")
        )

        super.onDestroy()
    }

    private fun registerScreenEventReceivers() {
        registerReceiver(screenUnlockReceiver, IntentFilter(Intent.ACTION_USER_PRESENT))
        registerReceiver(screenLockReceiver, IntentFilter(Intent.ACTION_SCREEN_OFF))
        registerReceiver(screenOnReceiver, IntentFilter(Intent.ACTION_SCREEN_ON))
    }

    private fun unregisterScreenEventReceivers() {
        unregisterReceiver(screenUnlockReceiver)
        unregisterReceiver(screenLockReceiver)
        unregisterReceiver(screenOnReceiver)
    }

    override fun onBind(intent: Intent): IBinder? = null
}