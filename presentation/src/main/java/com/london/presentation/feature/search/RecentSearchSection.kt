package com.london.presentation.feature.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.london.presentation.feature.search.composable.RecentSearchesSection
import com.london.presentation.feature.search.composable.RecentViewedSection

@Composable
fun RecentSearchSection(
    state: SearchUiState,
    contract: SearchContract,
    onNavigateToTvShowDetails: (Int) -> Unit,
    onNavigateToMovieDetails: (Int) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val handleRecentSearchClick: (String) -> Unit = { query ->
        focusManager.clearFocus()
        keyboardController?.hide()
        contract.onRecentSearchClick(query)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        if (state.recentViewed.isNotEmpty()) {
            item {
                RecentViewedSection(
                    recentViewed = state.recentViewed,
                    onClearAll = contract::clearRecentViewed,
                    onNavigateToTvShowDetails = onNavigateToTvShowDetails,
                    onNavigateToMovieDetails = onNavigateToMovieDetails
                )
            }
        }

        if (state.recentSearches.isNotEmpty()) {
            item {
                RecentSearchesSection(
                    recentSearches = state.recentSearches,
                    onClearAll = contract::clearRecentSearches,
                    onSearchClick = handleRecentSearchClick,
                    onRemoveClick = contract::removeRecentSearch
                )
            }
        }
    }
}