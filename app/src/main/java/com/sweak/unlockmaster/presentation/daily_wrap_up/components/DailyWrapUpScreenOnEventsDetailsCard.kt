package com.sweak.unlockmaster.presentation.daily_wrap_up.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.ui.theme.space

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DailyWrapUpScreenOnEventsDetailsCard(
    modifier: Modifier = Modifier
) {
    Card(
        elevation = MaterialTheme.space.xSmall,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = MaterialTheme.space.smallMedium,
                    top = MaterialTheme.space.smallMedium,
                    end = MaterialTheme.space.smallMedium,
                    bottom = MaterialTheme.space.small,
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = MaterialTheme.space.small)
            ) {
                Text(
                    text = stringResource(R.string.screen_on_events),
                    style = MaterialTheme.typography.h1,
                    modifier = Modifier.padding(end = MaterialTheme.space.xSmall)
                )

                CompositionLocalProvider(
                    LocalMinimumInteractiveComponentEnforcement provides false
                ) {
                    IconButton(
                        onClick = {
                            // TODO: show dialog
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.HelpOutline,
                            contentDescription = stringResource(
                                R.string.content_description_help_icon
                            ),
                            modifier = Modifier.size(size = MaterialTheme.space.smallMedium)
                        )
                    }
                }
            }

            Row {
                Text(
                    text = "49",
                    style = MaterialTheme.typography.h1,
                    fontSize = 32.sp,
                    modifier = Modifier
                        .padding(end = MaterialTheme.space.xSmall)
                        .alignByBaseline()
                )

                Text(
                    text = stringResource(R.string.screen_turn_ons_which_is),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.alignByBaseline()
                )
            }

            Row {
                Text(
                    text = "—",
                    style = MaterialTheme.typography.h1,
                    fontSize = 32.sp,
                    modifier = Modifier
                        .padding(end = MaterialTheme.space.xSmall)
                        .alignByBaseline()
                )

                Text(
                    text = stringResource(R.string.as_much_as_yesterday_and),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.alignByBaseline()
                )
            }

            Row {
                Text(
                    text = "3",
                    style = MaterialTheme.typography.h1,
                    fontSize = 32.sp,
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier
                        .padding(end = MaterialTheme.space.xSmall)
                        .alignByBaseline()
                )

                Text(
                    text = stringResource(R.string.less_than_a_week_before),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.alignByBaseline()
                )
            }
        }
    }
}