package com.sweak.unlockmaster.presentation.widget

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.layout.Alignment.Companion.Center
import androidx.glance.layout.Box
import androidx.glance.layout.padding
import androidx.glance.layout.wrapContentSize
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.sweak.unlockmaster.presentation.common.theme.space

class UnlockCountWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Box(
                modifier = GlanceModifier
                    .wrapContentSize()
                    .padding(
                        top = MaterialTheme.space.mediumLarge,
                        bottom = MaterialTheme.space.medium
                    ),
                contentAlignment = Center
            ) {
                val unlockCount = currentState(UNLOCK_EVENTS_COUNT_PREFERENCES_KEY) ?: 0

                Text(
                    text = unlockCount.toString(),
                    style = TextStyle(
                        color = ColorProvider(Color.White),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                )
            }
        }
    }

    companion object {
        val UNLOCK_EVENTS_COUNT_PREFERENCES_KEY = intPreferencesKey("unlockEventsCountKey")
    }
}