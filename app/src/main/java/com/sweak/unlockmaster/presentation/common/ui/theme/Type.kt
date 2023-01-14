package com.sweak.unlockmaster.presentation.common.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sweak.unlockmaster.R

val amikoFamily = FontFamily(
    Font(R.font.amiko_regular, FontWeight.Normal),
    Font(R.font.amiko_semibold, FontWeight.SemiBold),
    Font(R.font.amiko_bold, FontWeight.Bold)
)

val Typography = Typography(
    h1 = TextStyle(
        fontFamily = amikoFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    h2 = TextStyle(
        fontFamily = amikoFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    h3 = TextStyle(
        fontFamily = amikoFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    h4 = TextStyle(
        fontFamily = amikoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = amikoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = amikoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    ),
    button = TextStyle(
        fontFamily = amikoFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    )
)