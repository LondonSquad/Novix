@file:KoverIgnore
package com.london.data.utils

import com.london.domain.KoverIgnore
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

 fun getCurrentDate(): String = Clock.System.now()
    .toLocalDateTime(TimeZone.currentSystemDefault())
    .date.toString()