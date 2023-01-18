package com.sweak.unlockmaster.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sweak.unlockmaster.presentation.common.ui.theme.space

@Composable
fun Dialog(
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    onPositiveClick: () -> Unit,
    positiveButtonText: String,
    onNegativeClick: (() -> Unit)? = null,
    negativeButtonText: String
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colors.surface)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h2,
                modifier = Modifier.padding(
                    start = MaterialTheme.space.medium,
                    top = MaterialTheme.space.medium,
                    end = MaterialTheme.space.medium,
                    bottom = MaterialTheme.space.small
                )
            )

            Text(
                text = message,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(
                    start = MaterialTheme.space.medium,
                    end = MaterialTheme.space.medium,
                    bottom = MaterialTheme.space.medium
                )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = MaterialTheme.space.medium,
                        end = MaterialTheme.space.medium,
                        bottom = MaterialTheme.space.medium
                    )
            ) {
                Button(
                    onClick = onNegativeClick ?: onDismissRequest,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = MaterialTheme.space.default
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = negativeButtonText,
                        modifier = Modifier.padding(all = MaterialTheme.space.xSmall)
                    )
                }

                Spacer(modifier = Modifier.width(MaterialTheme.space.medium))

                Button(
                    onClick = onPositiveClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = positiveButtonText,
                        modifier = Modifier.padding(MaterialTheme.space.xSmall)
                    )
                }
            }
        }
    }
}