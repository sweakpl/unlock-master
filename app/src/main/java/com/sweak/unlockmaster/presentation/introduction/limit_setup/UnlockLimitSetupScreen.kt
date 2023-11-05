package com.sweak.unlockmaster.presentation.introduction.limit_setup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ModeEdit
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.components.Dialog
import com.sweak.unlockmaster.presentation.common.components.InformationCard
import com.sweak.unlockmaster.presentation.common.components.NavigationBar
import com.sweak.unlockmaster.presentation.common.components.ProceedButton
import com.sweak.unlockmaster.presentation.common.theme.space
import com.sweak.unlockmaster.presentation.common.util.navigateThrottled
import com.sweak.unlockmaster.presentation.common.util.popBackStackThrottled
import com.sweak.unlockmaster.presentation.introduction.components.NumberPickerSlider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnlockLimitSetupScreen(
    unlockLimitSetupViewModel: UnlockLimitSetupViewModel = hiltViewModel(),
    navController: NavController,
    isUpdatingExistingUnlockLimit: Boolean
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = context) {
        unlockLimitSetupViewModel.unlockLimitSubmittedEvents.collect {
            if (isUpdatingExistingUnlockLimit) {
                navController.popBackStackThrottled(lifecycleOwner)
            } else {
                navController.navigateThrottled(
                    Screen.WorkInBackgroundScreen.withArguments(false.toString()),
                    lifecycleOwner
                )
            }
        }
    }

    val unlockLimitSetupScreenState = unlockLimitSetupViewModel.state
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(
            connection = scrollBehavior.nestedScrollConnection
        ),
        topBar = {
            NavigationBar(
                title = stringResource(R.string.unlock_limit),
                onNavigationButtonClick = { navController.popBackStackThrottled(lifecycleOwner) },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ProceedButton(
                text = stringResource(R.string.confirm),
                onClick = {
                    unlockLimitSetupViewModel.onEvent(
                        UnlockLimitSetupScreenEvent.SubmitSelectedUnlockLimit
                    )
                },
                modifier = Modifier.padding(horizontal = MaterialTheme.space.medium)
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = it)
        ) {
            Column(
                modifier = Modifier
                    .matchParentSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(
                        if (isUpdatingExistingUnlockLimit) R.string.update_your_unlock_limit
                        else R.string.set_up_unlock_limit
                    ),
                    style = MaterialTheme.typography.displayLarge,
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
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.space.medium,
                            end = MaterialTheme.space.medium,
                            bottom = MaterialTheme.space.medium
                        )
                )

                if (unlockLimitSetupScreenState.pickedUnlockLimit != null &&
                    unlockLimitSetupScreenState.availableUnlockLimitRange != null
                ) {
                    NumberPickerSlider(
                        pickedNumber = unlockLimitSetupScreenState.pickedUnlockLimit,
                        numbersRange = unlockLimitSetupScreenState.availableUnlockLimitRange,
                        onNewNumberPicked = { newUnlockLimit ->
                            unlockLimitSetupViewModel.onEvent(
                                UnlockLimitSetupScreenEvent.PickNewUnlockLimit(newUnlockLimit)
                            )
                        },
                        modifier = Modifier
                            .padding(
                                start = MaterialTheme.space.medium,
                                end = MaterialTheme.space.medium,
                                bottom = MaterialTheme.space.large,
                            )
                    )
                }

                if (unlockLimitSetupScreenState.unlockLimitForTomorrow != null) {
                    OutlinedCard(
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = Color.Transparent
                        ),
                        border = BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.tertiary
                        ),
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
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(
                                        R.string.new_unlock_limit_set_for_tomorrow
                                    ),
                                    style = MaterialTheme.typography.titleSmall
                                )

                                Text(
                                    text =
                                    unlockLimitSetupScreenState.unlockLimitForTomorrow.toString(),
                                    style = MaterialTheme.typography.displayMedium
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
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(all = MaterialTheme.space.medium)
                )

                InformationCard(
                    title = stringResource(R.string.reference),
                    description = stringResource(R.string.reference_description),
                    icon = Icons.Outlined.MyLocation,
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
                    icon = Icons.Outlined.ModeEdit,
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

                OutlinedCard(
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.tertiary
                    ),
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
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(start = MaterialTheme.space.small)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.space.run { xLarge + 2 * medium }))
            }
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