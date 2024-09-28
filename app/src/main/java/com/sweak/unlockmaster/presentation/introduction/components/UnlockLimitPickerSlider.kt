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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.theme.space
import kotlin.math.roundToInt

@Composable
fun UnlockLimitPickerSlider(
    pickedLimit: Int,
    limitRange: IntRange,
    onNewLimitPicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = pickedLimit.toString(),
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 48.sp),
            modifier = Modifier.padding(bottom = MaterialTheme.space.small)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (pickedLimit > limitRange.first) {
                        onNewLimitPicked(pickedLimit - 1)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Remove,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = stringResource(R.string.content_description_subtract_icon)
                )
            }

            Slider(
                value = pickedLimit.toFloat(),
                onValueChange = {
                    onNewLimitPicked(it.roundToInt())
                },
                valueRange = limitRange.run { first.toFloat()..last.toFloat() },
                colors = SliderDefaults.colors(
                    inactiveTrackColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {
                    if (pickedLimit < limitRange.last) {
                        onNewLimitPicked(pickedLimit + 1)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = stringResource(R.string.content_description_add_icon)
                )
            }
        }
    }
}