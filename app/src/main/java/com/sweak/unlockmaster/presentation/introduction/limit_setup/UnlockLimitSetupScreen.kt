package com.sweak.unlockmaster.presentation.introduction.limit_setup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.components.Dialog
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import com.sweak.unlockmaster.presentation.introduction.components.InformationCard
import com.sweak.unlockmaster.presentation.introduction.components.NumberPickerSlider
import com.sweak.unlockmaster.presentation.introduction.components.ProceedButton

@Composable
fun UnlockLimitSetupScreen(
    unlockLimitSetupViewModel: UnlockLimitSetupViewModel = hiltViewModel(),
    navController: NavController,
    isUpdatingExistingUnlockLimit: Boolean
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        unlockLimitSetupViewModel.unlockEventsSubmittedEvents.collect {
            if (isUpdatingExistingUnlockLimit) {
                navController.popBackStack()
            } else {
                navController.navigate(Screen.WorkInBackgroundScreen.route)
            }
        }
    }

    val unlockLimitSetupScreenState = unlockLimitSetupViewModel.state

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
                    text = stringResource(
                        if (isUpdatingExistingUnlockLimit) R.string.update_your_unlock_limit
                        else R.string.set_up_unlock_limit
                    ),
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
                    text = stringResource(
                        if (isUpdatingExistingUnlockLimit)
                            R.string.update_your_unlock_limit_description
                        else R.string.set_up_unlock_limit_description
                    ),
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.medium
                        )
                )

                NumberPickerSlider(
                    pickedNumber = unlockLimitSetupScreenState.pickedUnlockLimit,
                    numbersRange = IntRange(start = 10, endInclusive = 70),
                    onNewNumberPicked = { newUnlockLimit ->
                        unlockLimitSetupViewModel.onEvent(
                            UnlockLimitSetupScreenEvent.NewUnlockLimitPicked(newUnlockLimit)
                        )
                    },
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.large,
                        )
                )

                if (unlockLimitSetupScreenState.unlockLimitForTomorrow != null) {
                    Card(
                        backgroundColor = Color.Transparent,
                        border = BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colors.secondary
                        ),
                        elevation = MaterialTheme.space.default,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = MaterialTheme.space.medium,
                                end = MaterialTheme.space.medium,
                                bottom = MaterialTheme.space.small
                            )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(
                                horizontal = MaterialTheme.space.smallMedium,
                                vertical = MaterialTheme.space.small
                            )
                        ) {
                            Column {
                                Text(
                                    text = stringResource(
                                        R.string.new_unlock_limit_set_for_tomorrow
                                    ),
                                    style = MaterialTheme.typography.subtitle2
                                )

                                Text(
                                    text =
                                    unlockLimitSetupScreenState.unlockLimitForTomorrow.toString(),
                                    style = MaterialTheme.typography.h2
                                )
                            }

                            IconButton(
                                onClick = {
                                    unlockLimitSetupViewModel.onEvent(
                                        UnlockLimitSetupScreenEvent.RemoveUnlockLimitForTomorrowDialogVisibilityChanged(
                                            isVisible = true
                                        )
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = stringResource(
                                        R.string.content_description_delete_icon
                                    ),
                                    modifier = Modifier.size(size = MaterialTheme.space.mediumLarge)
                                )
                            }
                        }
                    }
                }

                Text(
                    text = stringResource(R.string.unlock_limit_purposes),
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.padding(all = MaterialTheme.space.medium)
                )

                InformationCard(
                    title = stringResource(R.string.reference),
                    description = stringResource(R.string.reference_description),
                    icon = Icons.Filled.MyLocation,
                    iconContentDescription = stringResource(
                        R.string.content_description_crosshair_icon
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
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
                        R.string.content_description_edit_icon
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
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
                    elevation = MaterialTheme.space.default,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.space.medium)
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
                onClick = {
                    unlockLimitSetupViewModel.onEvent(
                        UnlockLimitSetupScreenEvent.SelectedUnlockLimitSubmitted(
                            isUpdating = isUpdatingExistingUnlockLimit
                        )
                    )
                },
                modifier = Modifier.padding(all = MaterialTheme.space.medium)
            )
        }
    }

    if (unlockLimitSetupScreenState.isRemoveUnlockLimitForTomorrowDialogVisible) {
        Dialog(
            title = stringResource(R.string.remove_tomorrow_unlock_limit),
            message = stringResource(R.string.remove_tomorrow_unlock_limit_description),
            onDismissRequest = {
                unlockLimitSetupViewModel.onEvent(
                    UnlockLimitSetupScreenEvent.RemoveUnlockLimitForTomorrowDialogVisibilityChanged(
                        isVisible = false
                    )
                )
            },
            onPositiveClick = {
                unlockLimitSetupViewModel.onEvent(
                    UnlockLimitSetupScreenEvent.ConfirmRemoveUnlockLimitForTomorrow
                )
            },
            positiveButtonText = stringResource(R.string.yes),
            negativeButtonText = stringResource(R.string.no)
        )
    }
}