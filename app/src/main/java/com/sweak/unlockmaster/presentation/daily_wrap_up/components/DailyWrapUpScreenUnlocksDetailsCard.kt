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
import androidx.compose.ui.unit.sp
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import kotlin.math.abs

@Composable
fun DailyWrapUpScreenUnlocksDetailsCard(
    detailsData: DailyWrapUpScreenUnlocksDetailsData,
    onInteraction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = MaterialTheme.space.xSmall,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(all = MaterialTheme.space.smallMedium)) {
            Text(
                text = stringResource(R.string.screen_unlocks),
                style = MaterialTheme.typography.h1,
                modifier = Modifier.padding(bottom = MaterialTheme.space.small)
            )

            Row {
                Text(
                    text = detailsData.screenUnlocksCount.toString(),
                    style = MaterialTheme.typography.h1,
                    fontSize = 32.sp,
                    modifier = Modifier
                        .padding(end = MaterialTheme.space.xSmall)
                        .alignByBaseline()
                )

                Text(
                    text = stringResource(
                        if (detailsData.yesterdayDifference != null) R.string.screen_unlocks_which_is
                        else R.string.screen_unlocks_today
                    ),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.alignByBaseline()
                )
            }

            detailsData.yesterdayDifference?.let {
                Row {
                    Text(
                        text = if (it == 0) "—" else abs(it).toString(),
                        style = MaterialTheme.typography.h1,
                        fontSize = 32.sp,
                        color = if (it < 0) MaterialTheme.colors.primaryVariant
                        else if (it > 0) MaterialTheme.colors.error
                        else MaterialTheme.colors.onBackground,
                        modifier = Modifier
                            .padding(end = MaterialTheme.space.xSmall)
                            .alignByBaseline()
                    )

                    Text(
                        text = stringResource(
                            if (detailsData.weekBeforeDifference == null) {
                                if (it < 0) R.string.less_than_yesterday
                                else if (it > 0) R.string.more_than_yesterday
                                else R.string.as_much_as_yesterday
                            } else {
                                if (it < 0) R.string.less_than_yesterday_and
                                else if (it > 0) R.string.more_than_yesterday_and
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
                        text = if (it == 0) "—" else abs(it).toString(),
                        style = MaterialTheme.typography.h1,
                        fontSize = 32.sp,
                        color = if (it < 0) MaterialTheme.colors.primaryVariant
                        else if (it > 0) MaterialTheme.colors.error
                        else MaterialTheme.colors.onBackground,
                        modifier = Modifier
                            .padding(end = MaterialTheme.space.xSmall)
                            .alignByBaseline()
                    )

                    Text(
                        text = stringResource(
                            if (it < 0) R.string.less_than_a_week_before
                            else if (it > 0) R.string.more_than_a_week_before
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
                        text = stringResource(R.string.compare_with_other_days),
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = MaterialTheme.space.smallMedium)
                    )

                    Button(onClick = onInteraction) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(R.string.statistics),
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

data class DailyWrapUpScreenUnlocksDetailsData(
    val screenUnlocksCount: Int,
    val yesterdayDifference: Int?,
    val weekBeforeDifference: Int?
)