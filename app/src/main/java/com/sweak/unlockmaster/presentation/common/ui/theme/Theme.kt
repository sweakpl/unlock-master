package com.sweak.unlockmaster.presentation.common.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val colorPalette = lightColorScheme(
    primary = Riptide,
    onPrimary = Color.Black,
    secondary = OceanGreen,
    onSecondary = Color.Black,
    tertiary = Lochmara,
    onTertiary = Color.Black,
    background = Porcelain,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    error = Monza,
    onError = Color.White
)

@Composable
fun UnlockMasterTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalSpace provides Space()) {
        MaterialTheme(
            colorScheme = colorPalette,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}