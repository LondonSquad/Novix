package com.london.data.utils

fun Int?.orZero() = this ?: 0

fun Double?.orZero() = this ?: 0.0

val Boolean?.isTrue
    get() = this == true
