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

fun Double?.roundToDecimal(): Double = runCatching {
    "%.1f".format(Locale.US, this).toDouble()
}.getOrDefault(0.0)

fun String?.asImageUrlOrEmpty() = this?.let { BuildConfig.IMAGE_URL + it }.orEmpty()

fun String?.asYoutubeUrlOrEmpty(): String = this?.let { BuildConfig.YOUTUBE_URL + it }.orEmpty()

fun String.generateHash(): String =
    MessageDigest.getInstance("MD5").digest(toByteArray()).joinToString("") { "%02x".format(it) }

fun String.extractYear() =
    takeIf { isNotEmpty() }?.split("-")?.first()?.toInt() ?: 0

fun Long.isDayExpired(): Boolean {
    val oneDayInMillis = 24 * 60 * 60 * 1000L
    val oneDayAgo = System.currentTimeMillis() - oneDayInMillis
    return System.currentTimeMillis() < oneDayAgo
}