package com.sweak.unlockmaster.presentation.introduction.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import kotlin.math.roundToInt

@Composable
fun NumberPickerSlider(
    pickedNumber: Int,
    numbersRange: IntRange,
    onNewNumberPicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = pickedNumber.toString(),
            style = MaterialTheme.typography.h1.copy(fontSize = 48.sp),
            modifier = Modifier.padding(bottom = MaterialTheme.space.small)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (pickedNumber > numbersRange.first) {
                        onNewNumberPicked(pickedNumber - 1)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Remove,
                    tint = MaterialTheme.colors.primary,
                    contentDescription = stringResource(R.string.content_description_subtract_icon)
                )
            }

            Slider(
                value = pickedNumber.toFloat(),
                onValueChange = {
                    onNewNumberPicked(it.roundToInt())
                },
                valueRange = numbersRange.run { first.toFloat()..last.toFloat() },
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {
                    if (pickedNumber < numbersRange.last) {
                        onNewNumberPicked(pickedNumber + 1)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    tint = MaterialTheme.colors.primary,
                    contentDescription = stringResource(R.string.content_description_add_icon)
                )
            }
        }
    }
}