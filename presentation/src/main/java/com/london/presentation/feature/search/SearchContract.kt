package com.london.presentation.feature.search

import androidx.compose.ui.text.input.TextFieldValue
import com.london.domain.entity.genre.Genre
import com.london.domain.entity.recent.RecentSearch
import com.london.domain.entity.recent.RecentViewed

interface SearchContract {
    fun onRetry()
    fun clearSearch()
    fun clearRecentViewed()
    fun clearRecentSearches()
    fun onMovieClick(movieId: Int)
    fun onActorClick(actorId: Int)
    fun onTvShowClick(tvShowId: Int)
    fun onSavedMovieClick(movie: MovieUi)
    fun onRecentSearchClick(search: String)
    fun addToRecentViewed(item: RecentViewed)
    fun onClickMovie(genresListId: List<Genre>)
    fun removeRecentSearch(search: RecentSearch)
    fun addToRecentSearches(query: RecentSearch)
    fun onCategorySelected(category: SearchCategory)
    fun onSearchQueryChange(newValue: TextFieldValue)
}
