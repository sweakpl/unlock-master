package com.sweak.unlockmaster.presentation.introduction.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
    Card(
        elevation = MaterialTheme.space.xSmall,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
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
                    style = MaterialTheme.typography.h4.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(bottom = MaterialTheme.space.xSmall)
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
}