package com.sweak.unlockmaster.presentation.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    negativeButtonText: String? = null
) {
    val onlyPositiveButton = negativeButtonText == null

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Surface(modifier = Modifier.clip(MaterialTheme.shapes.medium)) {
            Column(modifier = Modifier.wrapContentSize()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(
                        start = MaterialTheme.space.medium,
                        top = MaterialTheme.space.medium,
                        end = MaterialTheme.space.medium,
                        bottom = MaterialTheme.space.small
                    )
                )

                Text(
                    text = message,
                    style = MaterialTheme.typography.titleMedium,
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
                    if (!onlyPositiveButton) {
                        TextButton(
                            onClick = onNegativeClick ?: onDismissRequest,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = negativeButtonText!!,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.width(MaterialTheme.space.medium))
                    }

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
}