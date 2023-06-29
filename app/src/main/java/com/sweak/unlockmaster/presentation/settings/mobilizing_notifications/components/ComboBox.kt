package com.sweak.unlockmaster.presentation.settings.mobilizing_notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.ui.theme.space

@Composable
fun ComboBox(
    modifier: Modifier = Modifier,
    menuItems: List<Any>,
    selectedIndex: Int,
    onMenuItemClick: (Int) -> Unit
) {
    Column(modifier = modifier) {
        var expanded by remember { mutableStateOf(false) }

        Card {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
            ) {
                Text(
                    text = menuItems[selectedIndex].toString(),
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier
                        .padding(all = MaterialTheme.space.medium)
                        .weight(1f),
                )

                Icon(
                    imageVector =
                    if (expanded) Icons.Outlined.ArrowDropUp
                    else Icons.Outlined.ArrowDropDown,
                    contentDescription = stringResource(R.string.content_description_drop_down_arrow),
                    modifier = Modifier.padding(all = MaterialTheme.space.medium)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(
                MaterialTheme.space.xSmall,
                -MaterialTheme.space.xSmall
            ),
            modifier = Modifier
                .wrapContentWidth()
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            menuItems.forEachIndexed { index, content ->
                DropdownMenuItem(
                    onClick = {
                        onMenuItemClick(index)
                        expanded = false
                    }
                ) {
                    Text(
                        text = content.toString(),
                        style = MaterialTheme.typography.h2
                    )
                }
            }
        }
    }
}