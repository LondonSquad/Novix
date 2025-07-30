package com.london.data.utils

import com.london.data.BuildConfig
import java.security.MessageDigest
import java.util.Locale

fun Int?.orZero() = this ?: 0

fun Double?.orZero() = this ?: 0.0

fun Long?.orZero() = this ?: 0L

val Boolean?.isTrue
    get() = this == true

fun Double?.roundToFirstDecimal(): String = "%.1f".format(this)

fun Double?.roundToDecimal(): Double {
    if (this == null) return 0.0
    return "%.1f".format(Locale.US, this).toDouble()
}

fun String?.asImageUrlOrEmpty() = this?.let { BuildConfig.IMAGE_URL + it }.orEmpty()

fun String?.asYoutubeUrlOrEmpty() : String = this?.let { BuildConfig.YOUTUBE_URL + it }.orEmpty()

fun String.generateHash(): String =
    MessageDigest.getInstance("MD5").digest(toByteArray()).joinToString("") { "%02x".format(it) }
