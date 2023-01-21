package com.sweak.unlockmaster.presentation.main.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.components.OnResume
import com.sweak.unlockmaster.presentation.common.ui.theme.UnlockMasterTheme
import com.sweak.unlockmaster.presentation.common.ui.theme.space

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
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
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(
                            top = MaterialTheme.space.mediumLarge,
                            bottom = MaterialTheme.space.medium
                        )
                ) {
                    val progressBarStrokeWidth = MaterialTheme.space.small

                    CircularProgressIndicator(
                        progress = homeScreenState.run {
                            unlockCount!!.toFloat() / unlockLimit!!.toFloat()
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

                    IconButton(
                        onClick = { /* pause the unlock counter */ },
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Pause,
                            contentDescription = null
                        )
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

@Preview
@Composable
fun Preview() {
    UnlockMasterTheme {
        HomeScreen()
    }
}