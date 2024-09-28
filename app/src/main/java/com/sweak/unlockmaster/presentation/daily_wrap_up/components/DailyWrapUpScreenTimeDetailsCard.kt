package com.sweak.unlockmaster.presentation.daily_wrap_up.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.NavigateNext
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.theme.space
import com.sweak.unlockmaster.presentation.common.util.Duration
import com.sweak.unlockmaster.presentation.common.util.getCompactDurationString
import kotlin.math.abs

@Composable
fun DailyWrapUpScreenTimeDetailsCard(
    detailsData: DailyWrapUpScreenTimeDetailsData,
    onInteraction: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = MaterialTheme.space.xSmall
        ),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(all = MaterialTheme.space.smallMedium)) {
            Text(
                text = stringResource(R.string.screen_time),
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = MaterialTheme.space.small)
            )

            Row {
                Text(
                    text = getCompactDurationString(
                        Duration(
                            detailsData.screenTimeDurationMillis,
                            Duration.DisplayPrecision.MINUTES
                        )
                    ),
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 32.sp,
                    modifier = Modifier
                        .padding(end = MaterialTheme.space.xSmall)
                        .alignByBaseline()
                )

                Text(
                    text = stringResource(
                        if (detailsData.yesterdayDifferenceMillis != null) R.string.of_screen_time_which_is
                        else R.string.of_screen_today
                    ),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.alignByBaseline()
                )
            }

            val minuteInMillis = 60000L

            detailsData.yesterdayDifferenceMillis?.let { durationMillis ->
                Row {
                    Text(
                        text = if (durationMillis in -minuteInMillis..minuteInMillis) "—"
                        else getCompactDurationString(
                            Duration(
                                abs(durationMillis),
                                Duration.DisplayPrecision.MINUTES
                            )
                        ),
                        style = MaterialTheme.typography.displayLarge,
                        fontSize = 32.sp,
                        color = if (durationMillis < -minuteInMillis) MaterialTheme.colorScheme.secondary
                        else if (durationMillis > minuteInMillis) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(end = MaterialTheme.space.xSmall)
                            .alignByBaseline()
                    )

                    Text(
                        text = stringResource(
                            if (detailsData.weekBeforeDifferenceMillis == null) {
                                if (durationMillis < -minuteInMillis) R.string.less_than_yesterday
                                else if (durationMillis > minuteInMillis) R.string.more_than_yesterday
                                else R.string.as_much_as_yesterday
                            } else {
                                if (durationMillis < -minuteInMillis) R.string.less_than_yesterday_and
                                else if (durationMillis > minuteInMillis) R.string.more_than_yesterday_and
                                else R.string.as_much_as_yesterday_and
                            }
                        ),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.alignByBaseline()
                    )
                }
            }

            detailsData.weekBeforeDifferenceMillis?.let { durationMillis ->
                Row {
                    Text(
                        text = if (durationMillis in -minuteInMillis..minuteInMillis) "—"
                        else getCompactDurationString(
                            Duration(
                                abs(durationMillis),
                                Duration.DisplayPrecision.MINUTES
                            )
                        ),
                        style = MaterialTheme.typography.displayLarge,
                        fontSize = 32.sp,
                        color = if (durationMillis < -minuteInMillis) MaterialTheme.colorScheme.secondary
                        else if (durationMillis > minuteInMillis) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(end = MaterialTheme.space.xSmall)
                            .alignByBaseline()
                    )

                    Text(
                        text = stringResource(
                            if (durationMillis < -minuteInMillis) R.string.less_than_a_week_before
                            else if (durationMillis > minuteInMillis) R.string.more_than_a_week_before
                            else R.string.as_much_as_a_week_before
                        ),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.alignByBaseline()
                    )
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.space.medium))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {
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
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = MaterialTheme.space.smallMedium)
                    )

                    Button(onClick = onInteraction) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(R.string.details),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(end = MaterialTheme.space.small)
                            )

                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.NavigateNext,
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
    val screenTimeDurationMillis: Long,
    val yesterdayDifferenceMillis: Long?,
    val weekBeforeDifferenceMillis: Long?
)