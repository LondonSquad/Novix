package com.london.presentation.utils

import java.util.Locale

fun Any?.toLocalizedNumbers(): String {
    val currentLocale = Locale.getDefault()
    var text = ""
    runCatching {
        text = this.toString()
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
    return text
}

fun convertDate(input: String): String {
    var day = ""
    var month = ""
    var year = ""
    var monthName = ""
    runCatching {
        val parts = input.split("-")
        day = parts[2]
        month = parts[1]
        year = parts[0]
        val currentLang = Locale.getDefault().language
        monthName = when (currentLang) {
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
    }

    return "$day $monthName $year".toLocalizedNumbers()
}

fun getLocalizedTimeUnit(unit: String): String {
    val lang = Locale.getDefault().language
    val arMap = mapOf("h" to "س", "m" to "د", "s" to "ث")
    val enMap = mapOf("h" to "h", "m" to "m", "s" to "s")

    val key = unit.lowercase()
    return when (lang) {
        "ar" -> arMap[key] ?: unit
        else -> enMap[key] ?: unit
    }
}


fun reverseDateFormat(input: String): String {
    var day = ""
    var month = ""
    var year = ""
    runCatching {
        val parts = input.split("-")
        day = parts[2]
        month = parts[1]
        year = parts[0]
    }
    return "$day-$month-$year".toLocalizedNumbers()
}

fun String?.getValueOf(key: String): String? {
    return this
}