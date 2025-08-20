package com.london.presentation.feature.search

import androidx.compose.ui.text.input.TextFieldValue
import androidx.paging.PagingData
import com.london.domain.entity.actor.Actor
import com.london.domain.entity.movie.Movie
import com.london.domain.entity.recent.RecentSearch
import com.london.domain.entity.recent.RecentViewed
import com.london.domain.entity.tvshow.TvShow
import com.london.presentation.shared.base.ErrorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class SearchUiState(
    val lastSearch: String = "",
    val error: ErrorState? = null,
    val savedMovies: Set<Int> = emptySet(),
    val savedTvShows: Set<Int> = emptySet(),
    val showNoSearchBefore: Boolean = false,
    val showNoSearchResults: Boolean = false,
    val isSearchHistoryExpanded: Boolean = false,
    val searchHistory: List<String> = emptyList(),
    val actorsFlow: Flow<PagingData<Actor>> = flow {},
    val moviesFlow: Flow<PagingData<Movie>> = flow {},
    val isMovieSaved: (MovieUi) -> Boolean = { false },
    val recentViewed: List<RecentViewed> = emptyList(),
    val tvShowsFlow: Flow<PagingData<TvShow>> = flow {},
    val recentSearches: List<RecentSearch> = emptyList(),
    val searchQuery: TextFieldValue = TextFieldValue(""),
    val selectedCategory: SearchCategory = SearchCategory.Movies,
    val isBookmarkSheetVisible: Boolean = false,
    val bookmarkedMovieId: Int = 0,
)

data class MovieUi(
    val id: Int,
    val title: String,
    val isSaved: Boolean,
    val posterUrl: String,
)
