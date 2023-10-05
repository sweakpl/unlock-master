package com.sweak.unlockmaster.presentation.main.screen_time.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import com.sweak.unlockmaster.presentation.common.util.Duration
import com.sweak.unlockmaster.presentation.common.util.TimeFormat
import com.sweak.unlockmaster.presentation.common.util.getCompactDurationString
import com.sweak.unlockmaster.presentation.common.util.getTimeString

@Composable
fun SingleScreenTimeSessionCard(
    screenSessionStartAndEndTimesInMillis: Pair<Long, Long>,
    screenSessionDuration: Duration,
    timeFormat: TimeFormat,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = MaterialTheme.space.xSmall
        ),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = MaterialTheme.space.medium)
        ) {
            Icon(
                imageVector = Icons.Outlined.AccessTime,
                contentDescription = stringResource(R.string.content_description_clock_icon),
                modifier = Modifier.size(size = MaterialTheme.space.mediumLarge)
            )

            Text(
                text = getTimeString(screenSessionStartAndEndTimesInMillis.first, timeFormat) +
                        " - " +
                        getTimeString(screenSessionStartAndEndTimesInMillis.second, timeFormat),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.space.smallMedium)
                    .weight(1f)
            )

            Text(
                text = getCompactDurationString(screenSessionDuration),
                style = MaterialTheme.typography.displayMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}