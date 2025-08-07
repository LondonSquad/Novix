package com.london.presentation.shared

import androidx.compose.runtime.compositionLocalOf
import com.london.domain.contentrestriction.ContentRestrictionLevel

val LocalContentRestrictionLevel = compositionLocalOf { ContentRestrictionLevel.MODERATE }