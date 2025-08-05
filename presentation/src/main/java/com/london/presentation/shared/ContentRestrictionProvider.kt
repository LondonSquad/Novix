package com.london.presentation.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.london.domain.AppPreferencesService
import com.london.domain.contentrestriction.ContentRestrictionLevel

@Composable
fun ContentRestrictionProvider(
    appPreferencesService: AppPreferencesService,
    content: @Composable (ContentRestrictionLevel) -> Unit
) {
    val contentRestrictionLevel by appPreferencesService.contentRestrictionLevel.collectAsState()
    content(contentRestrictionLevel)
}