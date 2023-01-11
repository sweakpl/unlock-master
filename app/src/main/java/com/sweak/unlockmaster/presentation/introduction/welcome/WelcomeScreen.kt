package com.sweak.unlockmaster.presentation.introduction.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.sweak.unlockmaster.R
import com.sweak.unlockmaster.presentation.common.ui.theme.UnlockMasterTheme
import com.sweak.unlockmaster.presentation.common.ui.theme.space

@Composable
fun WelcomeScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colors.primary,
                        MaterialTheme.colors.primaryVariant
                    )
                )
            )
    ) {
        Image(
            painter = painterResource(R.drawable.ic_notification_icon),
            contentDescription = stringResource(R.string.content_description_application_icon),
            modifier = Modifier.size(MaterialTheme.space.xxxLarge)
        )

        Text(
            text = stringResource(R.string.welcome_to_unlock_master),
            style = MaterialTheme.typography.h1,
            color = Color.White,
            modifier = Modifier.padding(all = MaterialTheme.space.medium)
        )

        Text(
            text = stringResource(R.string.welcome_introduction_subtitle),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1,
            color = Color.White,
            modifier = Modifier
                .padding(
                    start = MaterialTheme.space.medium,
                    end = MaterialTheme.space.medium,
                    bottom = MaterialTheme.space.large
                )
        )

        Button(
            onClick = { /* no-op */ },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.surface
            ),
            elevation = ButtonDefaults.elevation(
                defaultElevation = MaterialTheme.space.xSmall
            ),
            contentPadding = PaddingValues(
                horizontal = MaterialTheme.space.xxLarge,
                vertical = MaterialTheme.space.small
            )
        ) {
            Text(
                text = stringResource(R.string.lets_start),
                style = MaterialTheme.typography.h1
            )
        }
    }
}

@Preview
@Composable
fun Preview() {
    UnlockMasterTheme {
        WelcomeScreen()
    }
}