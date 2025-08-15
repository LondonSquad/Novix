package com.london.presentation.feature.search

import androidx.compose.ui.text.input.TextFieldValue
import com.london.domain.entity.genre.Genre
import com.london.domain.entity.recent.RecentSearch
import com.london.domain.entity.recent.RecentViewed

interface SearchContract {
    fun onSearchQueryChange(newValue: TextFieldValue)
    fun onCategorySelected(category: SearchCategory)
    fun clearSearch()
    fun addToRecentSearches(query: RecentSearch)
    fun removeRecentSearch(search: RecentSearch)
    fun onRecentSearchClick(search: String)
    fun clearRecentSearches()
    fun addToRecentViewed(item: RecentViewed)
    fun clearRecentViewed()
    fun onActorClick(actorId: Int)
    fun onMovieClick(movieId: Int)
    fun onTvShowClick(tvShowId: Int)
    fun onMovieGenreClick(genresListId: List<Genre>)
    fun onSavedMovieClick(movie: MovieUi)
    fun onRetryClick()
    fun updateSearchState(updater: SearchUiState.() -> SearchUiState)
    fun performSearch(query: String, category: SearchCategory)
    fun incrementGenreInterest(genreId: Int, type: String)
    fun updateRecentData()
}

