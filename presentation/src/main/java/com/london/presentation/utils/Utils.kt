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

fun convertDate(input: String): String {
    val parts = input.split("-")
    val day = parts[2]
    val month = parts[1]
    val year = parts[0]

    val currentLang = Locale.getDefault().language

    val monthName = when (currentLang) {
        "ar" -> when (month) {
            "01" -> "يناير"
            "02" -> "فبراير"
            "03" -> "مارس"
            "04" -> "أبريل"
            "05" -> "مايو"
            "06" -> "يونيو"
            "07" -> "يوليو"
            "08" -> "أغسطس"
            "09" -> "سبتمبر"
            "10" -> "أكتوبر"
            "11" -> "نوفمبر"
            "12" -> "ديسمبر"
            else -> "غير معروف"
        }
        else -> when (month) {
            "01" -> "Jan"
            "02" -> "Feb"
            "03" -> "Mar"
            "04" -> "Apr"
            "05" -> "May"
            "06" -> "Jun"
            "07" -> "Jul"
            "08" -> "Aug"
            "09" -> "Sep"
            "10" -> "Oct"
            "11" -> "Nov"
            "12" -> "Dec"
            else -> "Invalid"
        }
    }

    return "$day $monthName $year".toLocalizedNumbers()
}
