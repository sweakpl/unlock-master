package com.sweak.unlockmaster.presentation.main.screen_time.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import com.sweak.unlockmaster.presentation.common.util.TimeFormat
import com.sweak.unlockmaster.presentation.common.util.getTimeString

@Composable
fun CounterPauseSeparator(
    counterPauseSessionStartAndEndTimesInMillis: Pair<Long, Long>,
    timeFormat: TimeFormat,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .background(color = Color.Black)
                .weight(1f)
        )

        Text(
            text = stringResource(R.string.counter_paused_colon) +
                    " " +
                    getTimeString(counterPauseSessionStartAndEndTimesInMillis.first, timeFormat) +
                    " - " +
                    getTimeString(counterPauseSessionStartAndEndTimesInMillis.second, timeFormat),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = MaterialTheme.space.small)
        )

        Spacer(
            modifier = Modifier
                .height(1.dp)
                .background(color = Color.Black)
                .weight(1f)
        )
    }
}