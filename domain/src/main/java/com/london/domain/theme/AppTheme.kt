package com.london.domain.theme

enum class AppTheme {
    LIGHT,
    DARK,
    SYSTEM
}

fun String.isDark(): Boolean = when (this) {
    AppTheme.DARK.name -> true
    else -> false
}

fun String.toAppTheme(): AppTheme = when (this) {
    AppTheme.DARK.name -> AppTheme.DARK
    AppTheme.LIGHT.name -> AppTheme.LIGHT
    else -> AppTheme.SYSTEM
}