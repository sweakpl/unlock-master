package com.sweak.unlockmaster.presentation.daily_wrap_up.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import com.sweak.unlockmaster.presentation.common.util.Duration
import com.sweak.unlockmaster.presentation.common.util.getCompactDurationString
import kotlin.math.abs

@Composable
fun DailyWrapUpScreenTimeDetailsCard(
    detailsData: DailyWrapUpScreenTimeDetailsData,
    onInteraction: () -> Unit,
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
                    text = getCompactDurationString(detailsData.screenTimeDuration),
                    style = MaterialTheme.typography.h1,
                    fontSize = 32.sp,
                    modifier = Modifier
                        .padding(end = MaterialTheme.space.xSmall)
                        .alignByBaseline()
                )

                Text(
                    text = stringResource(
                        if (detailsData.yesterdayDifference != null) R.string.of_screen_time_which_is
                        else R.string.of_screen_today
                    ),
                    style = MaterialTheme.typography.h4,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.alignByBaseline()
                )
            }

            val minuteInMillis = 60000

            detailsData.yesterdayDifference?.let {
                Row {
                    Text(
                        text = if (it.durationMillis in -minuteInMillis..minuteInMillis) "—"
                        else getCompactDurationString(
                            it.copy(durationMillis = abs(it.durationMillis))
                        ),
                        style = MaterialTheme.typography.h1,
                        fontSize = 32.sp,
                        color = if (it.durationMillis < -minuteInMillis) MaterialTheme.colors.primaryVariant
                        else if (it.durationMillis > minuteInMillis) MaterialTheme.colors.error
                        else MaterialTheme.colors.onBackground,
                        modifier = Modifier
                            .padding(end = MaterialTheme.space.xSmall)
                            .alignByBaseline()
                    )

                    Text(
                        text = stringResource(
                            if (detailsData.weekBeforeDifference == null) {
                                if (it.durationMillis < -minuteInMillis) R.string.less_than_yesterday
                                else if (it.durationMillis > minuteInMillis) R.string.more_than_yesterday
                                else R.string.as_much_as_yesterday
                            } else {
                                if (it.durationMillis < -minuteInMillis) R.string.less_than_yesterday_and
                                else if (it.durationMillis > minuteInMillis) R.string.more_than_yesterday_and
                                else R.string.as_much_as_yesterday_and
                            }
                        ),
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.alignByBaseline()
                    )
                }
            }

            detailsData.weekBeforeDifference?.let {
                Row {
                    Text(
                        text = if (it.durationMillis in -minuteInMillis..minuteInMillis) "—"
                        else getCompactDurationString(
                            it.copy(durationMillis = abs(it.durationMillis))
                        ),
                        style = MaterialTheme.typography.h1,
                        fontSize = 32.sp,
                        color = if (it.durationMillis < -minuteInMillis) MaterialTheme.colors.primaryVariant
                        else if (it.durationMillis > minuteInMillis) MaterialTheme.colors.error
                        else MaterialTheme.colors.onBackground,
                        modifier = Modifier
                            .padding(end = MaterialTheme.space.xSmall)
                            .alignByBaseline()
                    )

                    Text(
                        text = stringResource(
                            if (it.durationMillis < -minuteInMillis) R.string.less_than_a_week_before
                            else if (it.durationMillis > minuteInMillis) R.string.more_than_a_week_before
                            else R.string.as_much_as_a_week_before
                        ),
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.alignByBaseline()
                    )
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.space.medium))

            Card(backgroundColor = MaterialTheme.colors.background) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(all = MaterialTheme.space.smallMedium)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.TipsAndUpdates,
                        contentDescription = stringResource(R.string.content_description_tips_icon),
                        modifier = Modifier.size(size = MaterialTheme.space.mediumLarge)
                    )

                    Text(
                        text = stringResource(R.string.see_your_screen_time_breakdown),
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = MaterialTheme.space.smallMedium)
                    )

                    Button(onClick = onInteraction) {
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

data class DailyWrapUpScreenTimeDetailsData(
    val screenTimeDuration: Duration,
    val yesterdayDifference: Duration?,
    val weekBeforeDifference: Duration?
)