package com.london.presentation.utils

import java.util.Locale

fun Any?.toLocalizedNumbers(): String {
    val currentLocale = Locale.getDefault()
    val text = this.toString()

    return if (currentLocale.language == "ar") {
        text.map { char ->
            when (char) {
                '0' -> '٠'
                '1' -> '١'
                '2' -> '٢'
                '3' -> '٣'
                '4' -> '٤'
                '5' -> '٥'
                '6' -> '٦'
                '7' -> '٧'
                '8' -> '٨'
                '9' -> '٩'
                else -> char
            }
        }.joinToString("")
    } else {
        text
    }
}

