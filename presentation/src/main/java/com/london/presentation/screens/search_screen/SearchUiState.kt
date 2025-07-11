package com.london.presentation.screens.search_screen

import androidx.compose.runtime.Composable
import com.london.presentation.screens.search_screen.model.ActorUi
import com.london.presentation.screens.search_screen.model.MovieUi
import com.london.presentation.screens.search_screen.model.TvShowUi

data class SearchUiState(
    var searchQuery: String = "",
    val showNoSearchBefore: Boolean = false,
    val showNoSearchResults: Boolean = false,
    var showFilterBottomSheet: Boolean = false,
    val isSearchHistoryExpanded: Boolean = false,
    val isMovieSaved: (MovieUi) -> Boolean = { false },
    val searchHistory: List<String> = emptyList(),
    val actorUiResults: List<ActorUi> = emptyList(),
    val movieResults: List<MovieUi> = emptyList(),
    val tvShowUiResults: List<TvShowUi> = emptyList(),
    val savedMovies: Set<Int> = emptySet(),
    val selectedCategory: SearchCategory = SearchCategory.Movies
)

data class CategoryContent<T>(
    val items: List<T>,
    val content: @Composable (List<T>) -> Unit
)