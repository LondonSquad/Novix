package com.london.domain.theme

enum class AppTheme {
    LIGHT,
    DARK
}

fun AppTheme.isDark(): Boolean = when (this) {
    AppTheme.DARK -> true
    else -> false
}

fun String.toAppTheme(): AppTheme = when (this) {
    AppTheme.DARK.name -> AppTheme.DARK
    else -> AppTheme.LIGHT
}