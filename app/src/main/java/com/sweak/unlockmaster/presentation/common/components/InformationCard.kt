package com.sweak.unlockmaster.presentation.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.sweak.unlockmaster.presentation.common.ui.theme.space

@Composable
fun InformationCard(
    title: String,
    description: String,
    icon: ImageVector,
    iconContentDescription: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(MaterialTheme.space.medium))

            Icon(
                imageVector = icon,
                contentDescription = iconContentDescription,
                modifier = Modifier.size(size = MaterialTheme.space.xLarge)
            )

            Column(
                modifier = Modifier.padding(all = MaterialTheme.space.medium)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(bottom = MaterialTheme.space.xSmall)
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}