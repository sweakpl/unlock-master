package com.sweak.unlockmaster.presentation.main.home

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.Screen
import com.sweak.unlockmaster.presentation.common.components.OnResume
import com.sweak.unlockmaster.presentation.common.ui.theme.space
import com.sweak.unlockmaster.presentation.unlock_counter_service.EXTRA_IS_UNLOCK_COUNTER_PAUSED
import com.sweak.unlockmaster.presentation.unlock_counter_service.UNLOCK_COUNTER_PAUSE_CHANGED

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {
    OnResume {
        homeViewModel.refresh()
    }

    val homeScreenState = homeViewModel.state

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.space.xxLarge)
                .background(color = MaterialTheme.colors.primary)
        ) {
            IconButton(onClick = { /* navigate */ }) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = stringResource(R.string.content_description_menu_icon)
                )
            }

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.h2
            )

            Spacer(
                modifier = Modifier
                    .size(
                        width = MaterialTheme.space.xLarge,
                        height = MaterialTheme.space.mediumLarge
                    )
            )
        }

        AnimatedContent(
            targetState = homeScreenState.isInitializing,
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) { isInitializing ->
            if (!isInitializing) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(
                                top = MaterialTheme.space.mediumLarge,
                                bottom = MaterialTheme.space.medium
                            )
                            .align(alignment = Alignment.CenterHorizontally)
                    ) {
                        val progressBarStrokeWidth = MaterialTheme.space.small

                        CircularProgressIndicator(
                            progress = homeScreenState.run {
                                unlockCount.toFloat() / unlockLimit.toFloat()
                            },
                            color = MaterialTheme.colors.primaryVariant,
                            strokeWidth = progressBarStrokeWidth,
                            modifier = Modifier
                                .size(size = 216.dp)
                                .padding(all = MaterialTheme.space.smallMedium)
                                .drawBehind {
                                    drawCircle(
                                        color = Color.White,
                                        radius = size.width / 2 - progressBarStrokeWidth.toPx() / 2,
                                        style = Stroke(
                                            width = progressBarStrokeWidth.toPx()
                                        ),
                                    )
                                }
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Text(
                                text = homeScreenState.unlockCount.toString(),
                                style = MaterialTheme.typography.h1.copy(fontSize = 48.sp)
                            )

                            Text(
                                text = stringResource(R.string.unlocks),
                                style = MaterialTheme.typography.h3
                            )
                        }

                        val context = LocalContext.current

                        IconButton(
                            onClick = {
                                homeViewModel.onEvent(
                                    HomeScreenEvent.UnlockCounterPauseChanged {
                                        context.sendBroadcast(
                                            Intent(
                                                UNLOCK_COUNTER_PAUSE_CHANGED
                                            ).apply {
                                                putExtra(EXTRA_IS_UNLOCK_COUNTER_PAUSED, it)
                                            }
                                        )
                                    }
                                )
                            },
                            modifier = Modifier.align(Alignment.TopStart)
                        ) {
                            Icon(
                                imageVector =
                                if (homeScreenState.isUnlockCounterPaused) Icons.Filled.PlayArrow
                                else Icons.Filled.Pause,
                                contentDescription =
                                if (homeScreenState.isUnlockCounterPaused)
                                    stringResource(R.string.content_description_play_icon)
                                else stringResource(R.string.content_description_pause_icon)
                            )
                        }
                    }

                    Card(
                        elevation = MaterialTheme.space.xSmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = MaterialTheme.space.medium,
                                end = MaterialTheme.space.medium,
                                bottom = MaterialTheme.space.small
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = MaterialTheme.space.medium)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        bottom =
                                        if (homeScreenState.unlockLimitForTomorrow != null)
                                            MaterialTheme.space.smallMedium
                                        else MaterialTheme.space.default
                                    )
                            ) {
                                Column {
                                    Text(
                                        text = stringResource(R.string.todays_unlock_limit),
                                        style = MaterialTheme.typography.h4
                                    )

                                    Text(
                                        text = homeScreenState.unlockLimit.toString(),
                                        style = MaterialTheme.typography.h2
                                    )
                                }

                                Button(
                                    onClick = {
                                        navController.navigate(
                                            Screen.UnlockLimitSetupScreen.withArguments(
                                                true.toString()
                                            )
                                        )
                                    }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = stringResource(R.string.set_new),
                                            style = MaterialTheme.typography.subtitle1,
                                            modifier = Modifier
                                                .padding(end = MaterialTheme.space.small)
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

                            AnimatedVisibility(
                                visible = homeScreenState.unlockLimitForTomorrow != null,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Card(
                                    backgroundColor = Color.Transparent,
                                    border = BorderStroke(
                                        width = 2.dp,
                                        color = MaterialTheme.colors.secondary
                                    ),
                                    elevation = MaterialTheme.space.default,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            navController.navigate(
                                                Screen.UnlockLimitSetupScreen.withArguments(
                                                    true.toString()
                                                )
                                            )
                                        }
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(
                                            horizontal = MaterialTheme.space.smallMedium,
                                            vertical = MaterialTheme.space.small
                                        )
                                    ) {
                                        Text(
                                            text = stringResource(
                                                R.string.new_unlock_limit_set_for_tomorrow
                                            ),
                                            style = MaterialTheme.typography.subtitle2,
                                            modifier = Modifier
                                                .padding(end = MaterialTheme.space.small)
                                        )

                                        Text(
                                            text =
                                            homeScreenState.unlockLimitForTomorrow?.toString() ?: "",
                                            style = MaterialTheme.typography.h2
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxHeight()) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.primaryVariant,
                        modifier = Modifier
                            .size(MaterialTheme.space.xLarge)
                            .align(alignment = Alignment.Center)
                    )
                }
            }
        }
    }
}