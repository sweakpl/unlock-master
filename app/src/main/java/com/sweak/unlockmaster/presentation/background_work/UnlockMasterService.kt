package com.sweak.unlockmaster.presentation.background_work

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.domain.model.ScreenTimeLimitWarningState
import com.sweak.unlockmaster.domain.model.ScreenTimeLimitWarningState.NO_WARNINGS_FIRED
import com.sweak.unlockmaster.domain.model.ScreenTimeLimitWarningState.WARNING_15_MINUTES_TO_LIMIT_FIRED
import com.sweak.unlockmaster.domain.model.ScreenTimeLimitWarningState.WARNING_LIMIT_REACHED_FIRED
import com.sweak.unlockmaster.domain.repository.UserSessionRepository
import com.sweak.unlockmaster.domain.use_case.counter_pause.AddCounterPausedEventUseCase
import com.sweak.unlockmaster.domain.use_case.counter_pause.AddCounterUnpausedEventUseCase
import com.sweak.unlockmaster.domain.use_case.lock_events.AddLockEventUseCase
import com.sweak.unlockmaster.domain.use_case.lock_events.ShouldAddLockEventUseCase
import com.sweak.unlockmaster.domain.use_case.screen_on_events.AddScreenOnEventUseCase
import com.sweak.unlockmaster.domain.use_case.screen_time.GetScreenTimeDurationForGivenDayUseCase
import com.sweak.unlockmaster.domain.use_case.screen_time_limits.GetScreenTimeLimitMinutesForTodayUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_events.AddUnlockEventUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_events.GetUnlockEventsCountForGivenDayUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_limits.GetUnlockLimitAmountForTodayUseCase
import com.sweak.unlockmaster.presentation.MainActivity
import com.sweak.unlockmaster.presentation.background_work.global_receivers.ShutdownReceiver
import com.sweak.unlockmaster.presentation.background_work.global_receivers.screen_event_receivers.ScreenLockReceiver
import com.sweak.unlockmaster.presentation.background_work.global_receivers.screen_event_receivers.ScreenOnReceiver
import com.sweak.unlockmaster.presentation.background_work.global_receivers.screen_event_receivers.ScreenUnlockReceiver
import com.sweak.unlockmaster.presentation.background_work.local_receivers.ScreenTimeLimitStateReceiver
import com.sweak.unlockmaster.presentation.background_work.local_receivers.UnlockCounterPauseReceiver
import com.sweak.unlockmaster.presentation.common.util.Duration
import com.sweak.unlockmaster.presentation.common.util.getCompactDurationString
import com.sweak.unlockmaster.presentation.widget.UnlockCountWidget
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class UnlockMasterService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

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

    @Inject
    lateinit var getScreenTimeDurationForGivenDayUseCase: GetScreenTimeDurationForGivenDayUseCase

    @Inject
    lateinit var getScreenTimeLimitMinutesForTodayUseCase: GetScreenTimeLimitMinutesForTodayUseCase

    private val unlockCounterPauseReceiver = UnlockCounterPauseReceiver().apply {
        onCounterPauseChanged = { isPaused ->
            serviceScope.launch {
                if (isPaused) {
                    unregisterScreenEventReceivers()
                    addCounterPausedEventUseCase()
                    stopScreenTimeMonitoring()
                } else {
                    addCounterUnpausedEventUseCase()
                    registerScreenEventReceivers()
                    launchScreenTimeMonitoringIfEnabled()
                }

                try {
                    notificationManager.notify(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                            FOREGROUND_SERVICE_ID
                        else FOREGROUND_SERVICE_NOTIFICATION_ID,
                        createNewServiceNotification()
                    )
                } catch (_: SecurityException) { /* no-op */ }
            }
        }
    }

    private val screenTimeLimitStateReceiver = ScreenTimeLimitStateReceiver().apply {
        onScreenTimeLimitStateChanged = { isEnabled ->
            serviceScope.launch {
                if (isEnabled && !userSessionRepository.isUnlockCounterPaused()) {
                    launchScreenTimeMonitoringIfEnabled()
                } else {
                    stopScreenTimeMonitoring()
                }
            }
        }
    }

    private val screenUnlockReceiver = ScreenUnlockReceiver().apply {
        onScreenUnlock = {
            serviceScope.launch {
                addUnlockEventUseCase()
                launchScreenTimeMonitoringIfEnabled()

                try {
                    notificationManager.notify(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) FOREGROUND_SERVICE_ID
                        else FOREGROUND_SERVICE_NOTIFICATION_ID,
                        createNewServiceNotification()
                    )

                    showUnlockLimitMobilizingNotificationIfNeeded()
                } catch (_: SecurityException) { /* no-op */ }

                updateUnlockCountWidgetIfRequired()
            }
        }
    }

    private suspend fun updateUnlockCountWidgetIfRequired() {
        GlanceAppWidgetManager(applicationContext)
            .getGlanceIds(UnlockCountWidget::class.java).also {
                it.ifEmpty { return }
            }

        sendBroadcast(
            Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE).apply {
                setPackage(packageName)
            }
        )
    }

    private val screenLockReceiver = ScreenLockReceiver().apply {
        onScreenLock = {
            serviceScope.launch {
                if (shouldAddLockEventUseCase()) {
                    addLockEventUseCase()
                    stopScreenTimeMonitoring()
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

    private var screenTimeMonitoringJob: Job? = null

    private suspend fun launchScreenTimeMonitoringIfEnabled() {
        if (!userSessionRepository.isScreenTimeLimitEnabled()) {
            return
        }

        screenTimeMonitoringJob?.cancel()
        screenTimeMonitoringJob = serviceScope.launch {
            val secondInMillis = 1000L
            val minuteInMillis = 60 * secondInMillis

            val millisUntilNextFullMinute =
                getScreenTimeDurationForGivenDayUseCase() % minuteInMillis

            // First, wait for the next full minute of screen time to update the service
            // notification with up-to-date screen time:
            delay(millisUntilNextFullMinute)

            while (true) {
                try {
                    notificationManager.notify(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) FOREGROUND_SERVICE_ID
                        else FOREGROUND_SERVICE_NOTIFICATION_ID,
                        createNewServiceNotification()
                    )
                } catch (_: SecurityException) { /* no-op */ }

                val screenTimeLimitMillis =
                    getScreenTimeLimitMinutesForTodayUseCase() * minuteInMillis
                val currentScreenTimeMillis = getScreenTimeDurationForGivenDayUseCase()
                val timeLeftUntilLimit = screenTimeLimitMillis - currentScreenTimeMillis
                val screenTimeLimitWarningState =
                    userSessionRepository.getScreenTimeLimitWarningState()

                if (timeLeftUntilLimit <= 0) { // Limit reached
                    if (screenTimeLimitWarningState != WARNING_LIMIT_REACHED_FIRED) {
                        userSessionRepository
                            .setScreenTimeLimitWarningState(WARNING_LIMIT_REACHED_FIRED)
                        showScreenTimeLimitNotification(WARNING_LIMIT_REACHED_FIRED)
                    }
                } else if (timeLeftUntilLimit <= 15 * minuteInMillis) { // Less than 15 minutes
                    if (screenTimeLimitWarningState != WARNING_15_MINUTES_TO_LIMIT_FIRED) {
                        userSessionRepository
                            .setScreenTimeLimitWarningState(WARNING_15_MINUTES_TO_LIMIT_FIRED)
                        showScreenTimeLimitNotification(WARNING_15_MINUTES_TO_LIMIT_FIRED)
                    }
                } else {
                    if (screenTimeLimitWarningState != NO_WARNINGS_FIRED) {
                        userSessionRepository.setScreenTimeLimitWarningState(NO_WARNINGS_FIRED)
                    }
                }

                // Update service notification with screen time and screen time limit every minute
                delay(minuteInMillis)
            }
        }
    }

    private fun stopScreenTimeMonitoring() = screenTimeMonitoringJob?.cancel()

    override fun onCreate() {
        super.onCreate()

        ContextCompat.registerReceiver(
            this,
            unlockCounterPauseReceiver,
            IntentFilter(ACTION_UNLOCK_COUNTER_PAUSE_CHANGED),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        ContextCompat.registerReceiver(
            this,
            screenTimeLimitStateReceiver,
            IntentFilter(ACTION_SCREEN_TIME_LIMIT_STATE_CHANGED),
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
                    try {
                        notificationManager.notify(
                            FOREGROUND_SERVICE_NOTIFICATION_ID,
                            createNewServiceNotification()
                        )
                    } catch (_: SecurityException) { /* no-op */ }
                }
            }

            if (!userSessionRepository.isUnlockCounterPaused()) {
                launchScreenTimeMonitoringIfEnabled()
            }
        }

        return START_STICKY
    }

    private suspend fun createNewServiceNotification(): Notification {
        val todayUnlockEventsCount = getUnlockEventsCountForGivenDayUseCase()
        val todayUnlockLimit = getUnlockLimitAmountForTodayUseCase()
        val isUnlockCounterPaused = userSessionRepository.isUnlockCounterPaused()
        val todayScreenTimeMillis = getScreenTimeDurationForGivenDayUseCase()

        val compactScreenTimeString = getCompactDurationString(
            duration = Duration(
                durationMillis = todayScreenTimeMillis,
                precision = Duration.DisplayPrecision.MINUTES
            ),
            resources = applicationContext.resources
        )

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
            setShowWhen(false)
            setSmallIcon(R.drawable.ic_notification_icon)
            setContentTitle(
                getString(
                    if (isUnlockCounterPaused) R.string.unlock_count_paused
                    else R.string.unlock_count,
                    todayUnlockEventsCount,
                    todayUnlockLimit
                )
            )
            val contentText = getString(
                R.string.your_unlock_count_is_your_screen_time_is,
                todayUnlockEventsCount,
                todayUnlockLimit,
                compactScreenTimeString
            )
            setContentText(contentText)
            setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
            setContentIntent(notificationPendingIntent)
            build()
        }
    }

    private suspend fun showUnlockLimitMobilizingNotificationIfNeeded() {
        val unlockCount = getUnlockEventsCountForGivenDayUseCase()
        val unlockLimit = getUnlockLimitAmountForTodayUseCase()
        val percentage = userSessionRepository.getMobilizingNotificationsFrequencyPercentage()
        val areOverLimitNotificationsEnabled =
            userSessionRepository.areOverUnlockLimitMobilizingNotificationsEnabled()

        val multiple = unlockLimit * percentage / 100f
        val maxMultiple = if (areOverLimitNotificationsEnabled) 1000 else 100
        val multiples = (percentage..maxMultiple step percentage).mapIndexed { index, _ ->
            ((index + 1) * multiple).roundToInt()
        }

        if (unlockCount in multiples) {
            try {
                notificationManager.notify(
                    UNLOCK_LIMIT_MOBILIZING_NOTIFICATION_ID,
                    createNewUnlockLimitMobilizingNotification(
                        (multiples.indexOf(unlockCount) + 1) * percentage,
                        unlockCount,
                        unlockLimit
                    )
                )
            } catch (_: SecurityException) { /* no-op */ }
        }
    }

    private fun createNewUnlockLimitMobilizingNotification(
        limitPercentageReached: Int,
        unlockCount: Int,
        unlockLimit: Int
    ): Notification {
        val notificationPendingIntent = PendingIntent.getActivity(
            applicationContext,
            UNLOCK_LIMIT_MOBILIZING_NOTIFICATION_REQUEST_CODE,
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

    private fun showScreenTimeLimitNotification(
        screenTimeLimitWarningState: ScreenTimeLimitWarningState
    ) {
        val notificationPendingIntent = PendingIntent.getActivity(
            applicationContext,
            SCREEN_TIME_LIMIT_MOBILIZING_NOTIFICATION_REQUEST_CODE,
            Intent(applicationContext, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        PendingIntent.FLAG_IMMUTABLE
                    else 0
        )
        val notificationTitle = getString(
            if (screenTimeLimitWarningState == WARNING_LIMIT_REACHED_FIRED) {
                R.string.screen_time_limit_reached
            } else {
                R.string.screen_time_limit_near
            }
        )
        val notificationDescription = getString(
            if (screenTimeLimitWarningState == WARNING_LIMIT_REACHED_FIRED) {
                R.string.screen_time_limit_reached_description
            } else {
                R.string.screen_time_limit_near_description
            }
        )

        val notification = NotificationCompat.Builder(
            applicationContext,
            MOBILIZING_NOTIFICATION_CHANNEL_ID
        ).run {
            priority = NotificationCompat.PRIORITY_HIGH
            setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            setSmallIcon(R.drawable.ic_notification_icon)
            setContentTitle(notificationTitle)
            setContentText(notificationDescription)
            setContentIntent(notificationPendingIntent)
            setAutoCancel(true)
            build()
        }

        try {
            notificationManager.notify(SCREEN_TIME_LIMIT_MOBILIZING_NOTIFICATION_ID, notification)
        } catch (_: SecurityException) { /* no-op */ }
    }

    override fun onDestroy() {
        runBlocking {
            userSessionRepository.setUnlockMasterServiceProperlyClosed(true)
        }

        unregisterScreenEventReceivers()
        unregisterReceiver(unlockCounterPauseReceiver)
        unregisterReceiver(screenTimeLimitStateReceiver)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            unregisterReceiver(shutdownReceiver)
        }

        stopScreenTimeMonitoring()
        serviceScope.cancel(CancellationException("UnlockMasterService has been destroyed"))

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