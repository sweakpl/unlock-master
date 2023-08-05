package com.sweak.unlockmaster.presentation.daily_wrap_up.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NavigateNext
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.ui.theme.space

@Composable
fun DailyWrapUpScreenTimeDetailsCard(
    modifier: Modifier = Modifier
) {
    Card(
        elevation = MaterialTheme.space.xSmall,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(all = MaterialTheme.space.smallMedium)) {
            Text(
                text = stringResource(R.string.screen_time),
                style = MaterialTheme.typography.h1,
                modifier = Modifier.padding(bottom = MaterialTheme.space.small)
            )

            Row {
                Text(
                    text = "1h 15 min",
                    style = MaterialTheme.typography.h1,
                    fontSize = 32.sp,
                    modifier = Modifier
                        .padding(end = MaterialTheme.space.xSmall)
                        .alignByBaseline()
                )

                Text(
                    text = stringResource(R.string.of_screen_time_which_is),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.alignByBaseline()
                )
            }

            Row {
                Text(
                    text = "13 min",
                    style = MaterialTheme.typography.h1,
                    fontSize = 32.sp,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier
                        .padding(end = MaterialTheme.space.xSmall)
                        .alignByBaseline()
                )

                Text(
                    text = stringResource(R.string.less_than_yesterday_and),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.alignByBaseline()
                )
            }

            Row(modifier = Modifier.padding(bottom = MaterialTheme.space.medium)) {
                Text(
                    text = "7 min",
                    style = MaterialTheme.typography.h1,
                    fontSize = 32.sp,
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier
                        .padding(end = MaterialTheme.space.xSmall)
                        .alignByBaseline()
                )

                Text(
                    text = stringResource(R.string.more_than_a_week_before),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.alignByBaseline()
                )
            }

            Card(backgroundColor = MaterialTheme.colors.background) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(all = MaterialTheme.space.smallMedium)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.TipsAndUpdates,
                        contentDescription = stringResource(R.string.content_description_tips),
                        modifier = Modifier.size(size = MaterialTheme.space.mediumLarge)
                    )

                    Text(
                        text = stringResource(R.string.see_your_screen_time_breakdown),
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = MaterialTheme.space.smallMedium)
                    )

                    Button(
                        onClick = {
                            // TODO: supply time of the daily wrap-up!
                            // navController.navigate(Screen.ScreenTimeScreen.route)
                        }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(R.string.details),
                                style = MaterialTheme.typography.subtitle1,
                                modifier = Modifier.padding(end = MaterialTheme.space.small)
                            )

                            Icon(
                                imageVector = Icons.Outlined.NavigateNext,
                                contentDescription = stringResource(
                                    R.string.content_description_next_icon
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}