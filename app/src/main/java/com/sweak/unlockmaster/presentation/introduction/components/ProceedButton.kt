package com.sweak.unlockmaster.presentation.introduction.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sweak.unlockmaster.presentation.common.ui.theme.space

@Composable
fun ProceedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        elevation = ButtonDefaults.elevation(
            defaultElevation = MaterialTheme.space.xSmall
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(MaterialTheme.space.xLarge)
    ) {
        Text(text = text)
    }
}