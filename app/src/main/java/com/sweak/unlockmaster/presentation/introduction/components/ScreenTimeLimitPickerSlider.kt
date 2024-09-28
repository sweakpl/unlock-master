package com.sweak.unlockmaster.presentation.introduction.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.theme.space
import com.sweak.unlockmaster.presentation.common.util.Duration
import com.sweak.unlockmaster.presentation.common.util.getCompactDurationString
import kotlin.math.roundToInt

@Composable
fun ScreenTimeLimitPickerSlider(
    pickedScreenTimeMinutes: Int,
    limitRange: IntRange,
    stepIntervalMinutes: Int,
    enabled: Boolean,
    onNewScreenTimeMinutesPicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val steps = remember { ((limitRange.last - limitRange.first) / 5) - 1 }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        val minuteInMillis = 60000L

        Text(
            text = getCompactDurationString(
                Duration(
                    pickedScreenTimeMinutes * minuteInMillis,
                    Duration.DisplayPrecision.MINUTES
                )
            ),
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 48.sp),
            modifier = Modifier
                .padding(bottom = MaterialTheme.space.small)
                .alpha(if (enabled) 1f else 0.5f)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (pickedScreenTimeMinutes > limitRange.first) {
                        onNewScreenTimeMinutesPicked(pickedScreenTimeMinutes - stepIntervalMinutes)
                    }
                },
                enabled = enabled
            ) {
                Icon(
                    imageVector = Icons.Outlined.Remove,
                    tint =
                    if (enabled) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    contentDescription = stringResource(R.string.content_description_subtract_icon)
                )
            }

            Slider(
                value = pickedScreenTimeMinutes.toFloat(),
                onValueChange = {
                    onNewScreenTimeMinutesPicked(it.roundToInt())
                },
                valueRange = limitRange.run { first.toFloat()..last.toFloat() },
                steps = steps,
                colors = SliderDefaults.colors(
                    inactiveTrackColor = MaterialTheme.colorScheme.surface,
                    activeTickColor = Color.Transparent,
                    inactiveTickColor = Color.Transparent,
                    disabledActiveTickColor = Color.Transparent,
                    disabledInactiveTickColor = Color.Transparent
                ),
                enabled = enabled,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {
                    if (pickedScreenTimeMinutes < limitRange.last) {
                        onNewScreenTimeMinutesPicked(pickedScreenTimeMinutes + stepIntervalMinutes)
                    }
                },
                enabled = enabled
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    tint =
                    if (enabled) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    contentDescription = stringResource(R.string.content_description_add_icon)
                )
            }
        }
    }
}