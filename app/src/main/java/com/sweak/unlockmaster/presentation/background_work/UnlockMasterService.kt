package com.sweak.unlockmaster.presentation.background_work

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import com.sweak.unlockmaster.domain.use_case.counter_pause.AddCounterPausedEventUseCase
import com.sweak.unlockmaster.domain.use_case.counter_pause.AddCounterUnpausedEventUseCase
import com.sweak.unlockmaster.domain.use_case.lock_events.AddLockEventUseCase
import com.sweak.unlockmaster.domain.use_case.lock_events.ShouldAddLockEventUseCase
import com.sweak.unlockmaster.domain.use_case.screen_on_events.AddScreenOnEventUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_events.AddUnlockEventUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_events.GetUnlockEventsCountForGivenDayUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_limits.GetUnlockLimitAmountForTodayUseCase
import com.sweak.unlockmaster.presentation.MainActivity
import com.sweak.unlockmaster.presentation.background_work.global_receivers.ShutdownReceiver
import com.sweak.unlockmaster.presentation.background_work.global_receivers.screen_event_receivers.ScreenLockReceiver
import com.sweak.unlockmaster.presentation.background_work.global_receivers.screen_event_receivers.ScreenOnReceiver
import com.sweak.unlockmaster.presentation.background_work.global_receivers.screen_event_receivers.ScreenUnlockReceiver
import com.sweak.unlockmaster.presentation.background_work.local_receivers.UnlockCounterPauseReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class UnlockMasterService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO)

    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    @Inject
    lateinit var userSessionRepository: UserSessionRepository

    @Inject
    lateinit var addUnlockEventUseCase: AddUnlockEventUseCase

    @Inject
    lateinit var addLockEventUseCase: AddLockEventUseCase

    @Inject
    lateinit var shouldAddLockEventUseCase: ShouldAddLockEventUseCase

    @Inject
    lateinit var addScreenOnEventUseCase: AddScreenOnEventUseCase

    @Inject
    lateinit var getUnlockEventsCountForGivenDayUseCase: GetUnlockEventsCountForGivenDayUseCase

    @Inject
    lateinit var getUnlockLimitAmountForTodayUseCase: GetUnlockLimitAmountForTodayUseCase

    @Inject
    lateinit var addCounterPausedEventUseCase: AddCounterPausedEventUseCase

    @Inject
    lateinit var addCounterUnpausedEventUseCase: AddCounterUnpausedEventUseCase

    private val unlockCounterPauseReceiver = UnlockCounterPauseReceiver().apply {
        onCounterPauseChanged = { isPaused ->
            serviceScope.launch {
                if (isPaused) {
                    unregisterScreenEventReceivers()
                    addCounterPausedEventUseCase()
                } else {
                    addCounterUnpausedEventUseCase()
                    registerScreenEventReceivers()
                }

                try {
                    notificationManager.notify(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                            FOREGROUND_SERVICE_ID
                        else FOREGROUND_SERVICE_NOTIFICATION_ID,
                        createNewServiceNotification(
                            getUnlockEventsCountForGivenDayUseCase(),
                            getUnlockLimitAmountForTodayUseCase(),
                            isPaused
                        )
                    )
                } catch (_: SecurityException) { /* no-op */ }
            }
        }
    }

    private val screenUnlockReceiver = ScreenUnlockReceiver().apply {
        onScreenUnlock = {
            serviceScope.launch {
                addUnlockEventUseCase()

                val currentUnlockCount = getUnlockEventsCountForGivenDayUseCase()
                val currentUnlockLimit = getUnlockLimitAmountForTodayUseCase()
                val mobilizingNotificationsFrequencyPercentage =
                    userSessionRepository.getMobilizingNotificationsFrequencyPercentage()

                try {
                    notificationManager.notify(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) FOREGROUND_SERVICE_ID
                        else FOREGROUND_SERVICE_NOTIFICATION_ID,
                        createNewServiceNotification(currentUnlockCount, currentUnlockLimit, false)
                    )

                    showMobilizingNotificationIfNeeded(
                        currentUnlockCount,
                        currentUnlockLimit,
                        mobilizingNotificationsFrequencyPercentage
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
                addScreenOnEventUseCase()
            }
        }
    }

    private val shutdownReceiver by lazy { ShutdownReceiver() }

    override fun onCreate() {
        super.onCreate()

        ContextCompat.registerReceiver(
            this,
            unlockCounterPauseReceiver,
            IntentFilter(ACTION_UNLOCK_COUNTER_PAUSE_CHANGED),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        serviceScope.launch {
            if (!userSessionRepository.isUnlockCounterPaused()) {
                registerScreenEventReceivers()
            }

            userSessionRepository.setUnlockMasterServiceProperlyClosed(false)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ContextCompat.registerReceiver(
                this,
                shutdownReceiver,
                IntentFilter().apply {
                    addAction("android.intent.action.ACTION_SHUTDOWN")
                    addAction("android.intent.action.QUICKBOOT_POWEROFF")
                },
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceScope.launch {
            val todayUnlockEventsCount = getUnlockEventsCountForGivenDayUseCase()
            val todayUnlockLimit = getUnlockLimitAmountForTodayUseCase()
            val isUnlockCounterPaused = userSessionRepository.isUnlockCounterPaused()

            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                    startForeground(
                        FOREGROUND_SERVICE_ID,
                        createNewServiceNotification(
                            todayUnlockEventsCount,
                            todayUnlockLimit,
                            isUnlockCounterPaused
                        ),
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST
                    )
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    startForeground(
                        FOREGROUND_SERVICE_ID,
                        createNewServiceNotification(
                            todayUnlockEventsCount,
                            todayUnlockLimit,
                            isUnlockCounterPaused
                        )
                    )
                }
                else -> {
                    try {
                        notificationManager.notify(
                            FOREGROUND_SERVICE_NOTIFICATION_ID,
                            createNewServiceNotification(
                                todayUnlockEventsCount,
                                todayUnlockLimit,
                                isUnlockCounterPaused
                            )
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
        isUnlockCounterPaused: Boolean
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

        return NotificationCompat.Builder(
            applicationContext,
            FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID
        ).run {
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
            build()
        }
    }

    private fun showMobilizingNotificationIfNeeded(
        unlockCount: Int,
        unlockLimit: Int,
        percentage: Int
    ) {
        val multiple = unlockLimit * percentage / 100f
        val multiples = (percentage..100 step percentage).mapIndexed { index, _ ->
            ((index + 1) * multiple).roundToInt()
        }

        if (unlockCount in multiples) {
            try {
                notificationManager.notify(
                    MOBILIZING_NOTIFICATION_ID,
                    createNewMobilizingNotification(
                        (multiples.indexOf(unlockCount) + 1) * percentage,
                        unlockCount,
                        unlockLimit
                    )
                )
            } catch (_: SecurityException) { /* no-op */ }
        }
    }

    private fun createNewMobilizingNotification(
        limitPercentageReached: Int,
        unlockCount: Int,
        unlockLimit: Int
    ): Notification {
        val notificationPendingIntent = PendingIntent.getActivity(
            applicationContext,
            MOBILIZING_NOTIFICATION_REQUEST_CODE,
            Intent(applicationContext, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        PendingIntent.FLAG_IMMUTABLE
                    else 0
        )

        return NotificationCompat.Builder(
            applicationContext,
            MOBILIZING_NOTIFICATION_CHANNEL_ID
        ).run {
            priority = NotificationCompat.PRIORITY_HIGH
            setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            setSmallIcon(R.drawable.ic_notification_icon)
            setContentTitle(getString(R.string.percent_of_limit_reached, limitPercentageReached))
            setContentText(getString(R.string.thats_your_unlock_number, unlockCount, unlockLimit))
            setContentIntent(notificationPendingIntent)
            setAutoCancel(true)
            build()
        }
    }

    override fun onDestroy() {
        runBlocking {
            userSessionRepository.setUnlockMasterServiceProperlyClosed(true)
        }

        unregisterScreenEventReceivers()
        unregisterReceiver(unlockCounterPauseReceiver)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            unregisterReceiver(shutdownReceiver)
        }

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