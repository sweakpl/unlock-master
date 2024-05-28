package com.sweak.unlockmaster.presentation.widget

import android.content.Context
import android.widget.RemoteViews
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.appwidget.AndroidRemoteViews
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Alignment.Companion.Center
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.size
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.sweak.unlockmaster.R

class UnlockCountWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(ColorProvider(Color(0xFFF3F4F5))),
                contentAlignment = Center
            ) {
                val unlockCount = currentState(UNLOCK_EVENTS_COUNT_PREFERENCES_KEY) ?: 0
                val unlockLimit = currentState(UNLOCK_LIMIT_PREFERENCES_KEY) ?: 1

                val remoteView = RemoteViews(context.packageName, R.layout.progress_bar)
                remoteView.setProgressBar(
                    R.id.progress_bar,
                    unlockLimit,
                    unlockCount,
                    false
                )

                AndroidRemoteViews(
                    remoteViews = remoteView,
                    modifier = GlanceModifier.size(size = 144.dp)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = unlockCount.toString(),
                        style = TextStyle(
                            color = ColorProvider(Color.Black),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )
                    )

                    Text(
                        text = LocalContext.current.resources.getQuantityString(
                            R.plurals.unlocks,
                            unlockCount
                        ),
                        style = TextStyle(
                            color = ColorProvider(Color.Black),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.SansSerif
                        )
                    )
                }
            }
        }
    }

    companion object {
        val UNLOCK_EVENTS_COUNT_PREFERENCES_KEY = intPreferencesKey("unlockEventsCountKey")
        val UNLOCK_LIMIT_PREFERENCES_KEY = intPreferencesKey("unlockLimitKey")
    }
}