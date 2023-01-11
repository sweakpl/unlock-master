package com.sweak.unlockmaster.presentation.common.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val colorPalette = lightColors(
    primary = Riptide,
    primaryVariant = OceanGreen,
    secondary = PictonBlue,
    secondaryVariant = Lochmara,
    background = Porcelain,
    surface = Color.White,
    error = Monza,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White
)

@Composable
fun UnlockMasterTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalSpace provides Space()) {
        MaterialTheme(
            colors = colorPalette,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}