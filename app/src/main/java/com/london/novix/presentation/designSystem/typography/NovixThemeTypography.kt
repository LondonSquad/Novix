package com.london.novix.presentation.designSystem.typography

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.london.novix.R

val NovixFont = FontFamily(
    Font(R.font.ibm_plex_sans_arabic_thin, FontWeight.Thin),
    Font(R.font.ibm_plex_sans_arabic_light, FontWeight.Light),
    Font(R.font.ibm_plex_sans_arabic_extralight, FontWeight.ExtraLight),
    Font(R.font.ibm_plex_sans_arabic, FontWeight.Normal),
    Font(R.font.ibm_plex_sans_arabic_medium, FontWeight.Medium),
    Font(R.font.ibm_plex_sans_arabic_semibold, FontWeight.SemiBold),
    Font(R.font.ibm_plex_sans_arabic_bold, FontWeight.Bold),
)

val NovixTextStyle = NovixTypography(
    headLineSmall = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = NovixFont,
        lineHeight = 30.sp
    ),
    headLineMedium = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = NovixFont,
        lineHeight = 36.sp
    ),
    headLineLarge = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = NovixFont,
        lineHeight = 42.sp
    ),
    titleSmall = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = NovixFont,
        lineHeight = 24.sp
    ),
    titleMedium = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = NovixFont,
        lineHeight = 28.sp
    ),
    titleLarge = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = NovixFont,
        lineHeight = 30.sp
    ),
    bodySmall = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = NovixFont,
        lineHeight = 22.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = NovixFont,
        lineHeight = 24.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = NovixFont,
        lineHeight = 28.sp
    ),
    labelSmall = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = NovixFont,
        lineHeight = 18.sp
    ),
    labelMedium = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = NovixFont,
        lineHeight = 22.sp
    ),
    labelLarge = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = NovixFont,
        lineHeight = 24.sp
    )
)