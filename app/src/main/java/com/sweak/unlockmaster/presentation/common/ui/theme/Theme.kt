package com.sweak.unlockmaster.presentation.common.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val lightColorPalette = lightColorScheme(
    primary = Riptide,
    onPrimary = Color.Black,
    secondary = OceanGreen,
    onSecondary = Color.Black,
    tertiary = Lochmara,
    onTertiary = Color.Black,
    background = Porcelain,
    onBackground = Color.Black,
    surface = Color.White,
    surfaceVariant = Color.White,
    onSurface = Color.Black,
    onSurfaceVariant = Color.Black,
    error = Monza,
    onError = Color.White
)

private val darkColorPalette = darkColorScheme(
    primary = DeepSea,
    onPrimary = Alto,
    secondary = OceanGreen,
    onSecondary = Color.Black,
    tertiary = Lochmara,
    onTertiary = Color.Black,
    background = MineShaft,
    onBackground = Alto,
    surface = GrayAsparagus,
    surfaceVariant = GrayAsparagus,
    onSurface = Alto,
    onSurfaceVariant = Alto,
    error = AlizarinCrimson,
    onError = Color.White
)

@Composable
fun UnlockMasterTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalSpace provides Space()) {
        val colorPalette = if (isSystemInDarkTheme()) darkColorPalette else lightColorPalette

        MaterialTheme(
            colorScheme = colorPalette,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}