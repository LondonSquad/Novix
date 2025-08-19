package com.london.presentation.shared

import androidx.compose.runtime.compositionLocalOf
import com.london.domain.entity.contentrestriction.ContentRestrictionLevel

val LocalContentRestrictionLevel = compositionLocalOf { ContentRestrictionLevel.MODERATE }