package com.sweak.unlockmaster.presentation.main.screen_time.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import com.sweak.unlockmaster.presentation.common.util.TimeFormat
import com.sweak.unlockmaster.presentation.common.util.getTimeString

@Composable
fun SingleScreenTimeSessionCard(
    screenSessionStartAndEndTimesInMillis: Pair<Long, Long>,
    screenSessionHoursMinutesAndSecondsDurationTriple: Triple<Int, Int, Int>,
    timeFormat: TimeFormat,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = MaterialTheme.space.xSmall,
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
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.space.smallMedium)
                    .weight(1f)
            )

            val screenTimeSessionDurationString = StringBuilder("").run {
                val hoursAmount = screenSessionHoursMinutesAndSecondsDurationTriple.first
                val minutesAmount = screenSessionHoursMinutesAndSecondsDurationTriple.second
                val secondsAmount = screenSessionHoursMinutesAndSecondsDurationTriple.third

                if (hoursAmount != 0) append(stringResource(R.string.hours_amount, hoursAmount))

                if (minutesAmount != 0) append(
                    (if (hoursAmount != 0) " " else "") +
                            stringResource(R.string.minutes_amount, minutesAmount)
                )

                if (secondsAmount != 0) append(
                    (if (hoursAmount != 0 || minutesAmount != 0) " " else "") +
                            stringResource(R.string.seconds_amount, secondsAmount)
                )

                toString()
            }

            Text(
                text = screenTimeSessionDurationString,
                style = MaterialTheme.typography.h2
            )
        }
    }
}