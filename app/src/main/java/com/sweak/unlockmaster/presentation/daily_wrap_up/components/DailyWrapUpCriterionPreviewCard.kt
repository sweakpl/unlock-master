package com.sweak.unlockmaster.presentation.daily_wrap_up.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.NavigateNext
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.AutoFixHigh
import androidx.compose.material.icons.outlined.HorizontalRule
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.theme.space
import com.sweak.unlockmaster.presentation.common.util.Duration
import com.sweak.unlockmaster.presentation.common.util.getCompactDurationString
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpCriterionPreviewType.Progress
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpCriterionPreviewType.ScreenOnEvents
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpCriterionPreviewType.ScreenTime
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpCriterionPreviewType.ScreenTimeLimit
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpCriterionPreviewType.ScreenUnlocks
import com.sweak.unlockmaster.presentation.daily_wrap_up.components.DailyWrapUpCriterionPreviewType.UnlockLimit

@Composable
fun DailyWrapUpCriterionPreviewCard(
    dailyWrapUpCriterionPreviewType: DailyWrapUpCriterionPreviewType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val criterionIcon: ImageVector
    val criterionIconContentDescription: String
    val progress: Progress?
    val isUnlockLimitSuggestionAvailable: Boolean
    val criterionValueText: String
    val criterionText: String

    when (dailyWrapUpCriterionPreviewType) {
        is ScreenUnlocks -> {
            criterionIcon = Icons.Outlined.LockOpen
            criterionIconContentDescription =
                stringResource(R.string.content_description_open_padlock_icon)
            progress = dailyWrapUpCriterionPreviewType.progress
            isUnlockLimitSuggestionAvailable = false
            criterionValueText = dailyWrapUpCriterionPreviewType.screenUnlocksCount.toString()
            criterionText = stringResource(R.string.screen_unlocks)
        }
        is ScreenTime -> {
            criterionIcon = Icons.Outlined.AccessTime
            criterionIconContentDescription =
                stringResource(R.string.content_description_clock_icon)
            progress = dailyWrapUpCriterionPreviewType.progress
            isUnlockLimitSuggestionAvailable = false
            criterionValueText = getCompactDurationString(
                Duration(
                    dailyWrapUpCriterionPreviewType.screenTimeDurationMillis,
                    Duration.DisplayPrecision.MINUTES
                )
            )
            criterionText = stringResource(R.string.screen_time)
        }
        is UnlockLimit -> {
            criterionIcon = Icons.Outlined.MyLocation
            criterionIconContentDescription =
                stringResource(R.string.content_description_crosshair_icon)
            progress = null
            isUnlockLimitSuggestionAvailable = dailyWrapUpCriterionPreviewType.isSuggestionAvailable
            criterionValueText = dailyWrapUpCriterionPreviewType.unlockLimitCount.toString()
            criterionText = stringResource(R.string.unlock_limit)
        }
        is ScreenTimeLimit -> {
            criterionIcon = Icons.Outlined.Alarm
            criterionIconContentDescription =
                stringResource(R.string.content_description_alarm_icon)
            progress = null
            isUnlockLimitSuggestionAvailable = dailyWrapUpCriterionPreviewType.isSuggestionAvailable
            criterionValueText = getCompactDurationString(
                Duration(
                    dailyWrapUpCriterionPreviewType.screenTimeLimitDurationMillis,
                    Duration.DisplayPrecision.MINUTES
                )
            )
            criterionText = stringResource(R.string.screen_time_limit)
        }
        is ScreenOnEvents -> {
            criterionIcon = Icons.Outlined.LightMode
            criterionIconContentDescription =
                stringResource(R.string.content_description_light_icon)
            progress = dailyWrapUpCriterionPreviewType.progress
            isUnlockLimitSuggestionAvailable = false
            criterionValueText = dailyWrapUpCriterionPreviewType.screenOnEventsCount.toString()
            criterionText = stringResource(R.string.screen_on_events)
        }
    }

    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = MaterialTheme.space.xSmall
        ),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = MaterialTheme.space.smallMedium,
                    top = MaterialTheme.space.smallMedium,
                    end = MaterialTheme.space.small,
                    bottom = MaterialTheme.space.smallMedium,
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = MaterialTheme.space.small)
            ) {
                Icon(
                    imageVector = criterionIcon,
                    contentDescription = criterionIconContentDescription,
                    modifier = Modifier.size(size = MaterialTheme.space.mediumLarge)
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.NavigateNext,
                    contentDescription = stringResource(R.string.content_description_next_icon),
                    modifier = Modifier.size(size = MaterialTheme.space.mediumLarge)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = MaterialTheme.space.xSmall)
            ) {
                if (progress != null) {
                    Icon(
                        imageVector = when (progress) {
                            Progress.STABLE -> Icons.Outlined.HorizontalRule
                            Progress.IMPROVEMENT -> Icons.AutoMirrored.Outlined.NavigateNext
                            Progress.REGRESS -> Icons.AutoMirrored.Outlined.NavigateNext
                        },
                        contentDescription = stringResource(
                            when (progress) {
                                Progress.STABLE -> R.string.content_description_horizontal_rule_icon
                                Progress.IMPROVEMENT -> R.string.content_description_improvement_icon
                                Progress.REGRESS -> R.string.content_description_regress_icon
                            }
                        ),
                        tint = when (progress) {
                            Progress.IMPROVEMENT -> MaterialTheme.colorScheme.secondary
                            Progress.REGRESS -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.onSurface
                        },
                        modifier = Modifier
                            .size(size = MaterialTheme.space.mediumLarge)
                            .rotate(
                                when (progress) {
                                    Progress.IMPROVEMENT -> 90f
                                    Progress.REGRESS -> -90f
                                    else -> 0f
                                }
                            )
                    )
                }

                Text(
                    text = criterionValueText,
                    style = MaterialTheme.typography.displayLarge
                )

                if (progress == null && isUnlockLimitSuggestionAvailable) {
                    Spacer(modifier = Modifier.width(MaterialTheme.space.xSmall))

                    Icon(
                        imageVector = Icons.Outlined.AutoFixHigh,
                        contentDescription = stringResource(
                            R.string.content_description_auto_change_icon
                        ),
                        modifier = Modifier.size(size = MaterialTheme.space.smallMedium)
                    )
                }
            }

            Text(
                text = criterionText,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

sealed class DailyWrapUpCriterionPreviewType {
    data class ScreenUnlocks(
        val screenUnlocksCount: Int,
        val progress: Progress
    ) : DailyWrapUpCriterionPreviewType()

    data class ScreenTime(
        val screenTimeDurationMillis: Long,
        val progress: Progress
    ) : DailyWrapUpCriterionPreviewType()

    data class UnlockLimit(
        val unlockLimitCount: Int,
        val isSuggestionAvailable: Boolean
    ) : DailyWrapUpCriterionPreviewType()

    data class ScreenTimeLimit(
        val screenTimeLimitDurationMillis: Long,
        val isSuggestionAvailable: Boolean
    ) : DailyWrapUpCriterionPreviewType()

    data class ScreenOnEvents(
        val screenOnEventsCount: Int,
        val progress: Progress
    ) : DailyWrapUpCriterionPreviewType()

    enum class Progress {
        IMPROVEMENT, REGRESS, STABLE
    }
}