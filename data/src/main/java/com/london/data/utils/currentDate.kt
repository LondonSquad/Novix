@file:KoverIgnore
package com.london.data.utils

import com.london.domain.KoverIgnore
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

fun getCurrentDate(): String = Clock.System.now()
    .toLocalDateTime(TimeZone.currentSystemDefault())
    .date.toString()
