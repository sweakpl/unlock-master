package com.sweak.unlockmaster.presentation.settings.daily_wrap_ups_setting.components

import android.annotation.SuppressLint
import android.os.Build
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.widget.TimePicker
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.ui.theme.space

@SuppressLint("InflateParams")
@Composable
fun CardTimePicker(
    hourOfDay: Int,
    minute: Int,
    onTimeChanged: (hourOfDay: Int, minute: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val is24HourFormat = DateFormat.is24HourFormat(context)

    Card(
        elevation = MaterialTheme.space.xSmall,
        modifier = modifier
    ) {
        AndroidView(
            factory = {
                (LayoutInflater.from(it)
                    .inflate(R.layout.spinner_time_picker, null) as TimePicker)
                    .apply {
                        // Right after composing the TimePicker it calls the  timeChangedListener
                        // with the current time which breaks the uiState - we have to prevent the
                        // uiState update after this initial timeChangedListener call:
                        var isInitialUpdate = true

                        setOnTimeChangedListener { _, hourOfDay, minute ->
                            if (!isInitialUpdate) {
                                onTimeChanged(hourOfDay, minute)
                            } else {
                                isInitialUpdate = false
                            }
                        }
                    }
            },
            update = {
                it.setIs24HourView(is24HourFormat)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    it.hour = hourOfDay
                    it.minute = minute
                } else {
                    it.currentHour = hourOfDay
                    it.currentMinute = minute
                }
            },
            modifier = Modifier.padding(all = MaterialTheme.space.medium)
        )
    }
}