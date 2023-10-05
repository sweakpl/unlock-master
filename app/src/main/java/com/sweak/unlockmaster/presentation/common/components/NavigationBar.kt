package com.sweak.unlockmaster.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.ui.theme.space

@Composable
fun NavigationBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(MaterialTheme.space.xxLarge)
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIos,
                contentDescription = stringResource(R.string.content_description_back_icon)
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(
            modifier = Modifier
                .size(
                    width = MaterialTheme.space.xLarge,
                    height = MaterialTheme.space.mediumLarge
                )
        )
    }
}