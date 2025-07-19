package com.london.presentation.screen.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.TextFieldValue
import androidx.paging.PagingData
import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.domain.entity.recent.RecentSearch
import com.london.domain.entity.recent.RecentViewed
import com.london.presentation.screen.search.model.MovieUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class SearchUiState(
    var searchQuery: TextFieldValue = TextFieldValue(""),
    val showNoSearchBefore: Boolean = false,
    val showNoSearchResults: Boolean = false,
    var showFilterBottomSheet: Boolean = false,
    val showFilterButton: Boolean = true,
    val isSearchHistoryExpanded: Boolean = false,
    val isMovieSaved: (MovieUi) -> Boolean = { false },
    val searchHistory: List<String> = emptyList(),
    val actorsFlow: Flow<PagingData<Actor>> = flow {},
    val moviesFlow: Flow<PagingData<Movie>> = flow {},
    val tvShowsFlow: Flow<PagingData<TvShow>> = flow {},
    val savedMovies: Set<Int> = emptySet(),
    val savedTvShows: Set<Int> = emptySet(),
    val selectedCategory: SearchCategory = SearchCategory.Movies,
    val recentViewed: List<RecentViewed> = emptyList(),
    val recentSearches: List<RecentSearch> = emptyList(),
    val lastSearch: String=""
)

data class CategoryContent<T>(
    val items: List<T>,
    val content: @Composable (List<T>) -> Unit
)