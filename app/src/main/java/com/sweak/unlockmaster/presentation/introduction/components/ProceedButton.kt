package com.sweak.unlockmaster.presentation.introduction.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sweak.unlockmaster.presentation.common.ui.theme.space

@Composable
fun ProceedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = MaterialTheme.space.xSmall
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(MaterialTheme.space.xLarge)
    ) {
        Text(text = text)
    }
}