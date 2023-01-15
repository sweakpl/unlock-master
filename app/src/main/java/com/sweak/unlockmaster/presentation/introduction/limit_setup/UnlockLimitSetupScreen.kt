package com.sweak.unlockmaster.presentation.introduction.limit_setup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import com.sweak.unlockmaster.presentation.introduction.components.InformationCard
import com.sweak.unlockmaster.presentation.introduction.components.NumberPickerSlider
import com.sweak.unlockmaster.presentation.introduction.components.ProceedButton

@Composable
fun UnlockLimitSetupScreen(navController: NavController) {

    var pickedUnlockLimit by remember { mutableStateOf(40) }

    Column(
        modifier = Modifier.background(color = MaterialTheme.colors.background)
    ) {
        NavigationBar(
            title = stringResource(R.string.unlock_limit),
            onBackClick = { navController.popBackStack() }
        )

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(R.string.set_up_unlock_limit),
                    style = MaterialTheme.typography.h1,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            top = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.small
                        )
                )

                Text(
                    text = stringResource(R.string.set_up_unlock_limit_description),
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.medium
                        )
                )

                NumberPickerSlider(
                    pickedNumber = pickedUnlockLimit,
                    numbersRange = IntRange(start = 10, endInclusive = 70),
                    onNewNumberPicked = { newUnlockLimit ->
                        pickedUnlockLimit = newUnlockLimit
                    },
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.xLarge,
                        )
                )

                Text(
                    text = stringResource(R.string.unlock_limit_purposes),
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.small
                        )
                )

                InformationCard(
                    title = stringResource(R.string.reference),
                    description = stringResource(R.string.reference_description),
                    icon = Icons.Filled.MyLocation,
                    iconContentDescription = stringResource(
                        R.string.content_description_statistics_icon
                    ),
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.medium
                        )
                )

                InformationCard(
                    title = stringResource(R.string.adjustability),
                    description = stringResource(R.string.adjustability_description),
                    icon = Icons.Filled.ModeEdit,
                    iconContentDescription = stringResource(
                        R.string.content_description_wellness_icon
                    ),
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.xLarge,
                        )
                )

                Spacer(modifier = Modifier.weight(1f))

                Card(
                    backgroundColor = Color.Transparent,
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colors.secondary
                    ),
                    modifier = Modifier.padding(horizontal = MaterialTheme.space.medium)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(all = MaterialTheme.space.small)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(
                                R.string.content_description_info_icon
                            )
                        )

                        Text(
                            text = stringResource(
                                R.string.note_reaching_limit_wont_prevent_unlocking
                            ),
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier.padding(start = MaterialTheme.space.small)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.space.run { xLarge + 2 * medium }))
            }

            ProceedButton(
                text = stringResource(R.string.confirm),
                onClick = { /* no-op */ },
                modifier = Modifier.padding(all = MaterialTheme.space.medium)
            )
        }
    }
}