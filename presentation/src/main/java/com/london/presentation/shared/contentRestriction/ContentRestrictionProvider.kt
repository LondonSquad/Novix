package com.london.presentation.shared.contentRestriction

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.london.domain.entity.contentrestriction.ContentRestrictionLevel
import com.london.domain.service.AppPreferencesService

@Composable
fun ContentRestrictionProvider(
    appPreferencesService: AppPreferencesService,
    content: @Composable (ContentRestrictionLevel) -> Unit
) {
    val contentRestrictionLevel by appPreferencesService.contentRestrictionLevel.collectAsState()
    content(contentRestrictionLevel)
}
