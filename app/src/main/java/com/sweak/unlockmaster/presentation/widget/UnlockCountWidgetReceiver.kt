package com.sweak.unlockmaster.presentation.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import com.sweak.unlockmaster.domain.use_case.unlock_events.GetUnlockEventsCountForGivenDayUseCase
import com.sweak.unlockmaster.domain.use_case.unlock_limits.GetUnlockLimitAmountForTodayUseCase
import com.sweak.unlockmaster.presentation.widget.UnlockCountWidget.Companion.UNLOCK_EVENTS_COUNT_PREFERENCES_KEY
import com.sweak.unlockmaster.presentation.widget.UnlockCountWidget.Companion.UNLOCK_LIMIT_PREFERENCES_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyAppWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = UnlockCountWidget()

    @Inject
    lateinit var getUnlockEventsCountForGivenDayUseCase: GetUnlockEventsCountForGivenDayUseCase

    @Inject
    lateinit var getUnlockLimitAmountForTodayUseCase: GetUnlockLimitAmountForTodayUseCase

    private val receiverScope = CoroutineScope(Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            receiverScope.launch {
                val glanceIds = GlanceAppWidgetManager(context)
                    .getGlanceIds(UnlockCountWidget::class.java).also {
                        it.ifEmpty { return@launch }
                    }
                val unlockEventsCount = getUnlockEventsCountForGivenDayUseCase()
                val unlockLimit = getUnlockLimitAmountForTodayUseCase()

                glanceIds.forEach {
                    updateAppWidgetState(context, it) { preferences ->
                        preferences[UNLOCK_EVENTS_COUNT_PREFERENCES_KEY] = unlockEventsCount
                        preferences[UNLOCK_LIMIT_PREFERENCES_KEY] = unlockLimit
                    }
                    glanceAppWidget.update(context, it)
                }
            }
        }
    }
}