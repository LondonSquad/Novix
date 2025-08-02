package com.london.presentation.feature.search

import androidx.compose.ui.text.input.TextFieldValue
import com.london.domain.entity.recent.RecentSearch
import com.london.domain.entity.recent.RecentViewed
import com.london.presentation.feature.search.model.MovieUi

interface SearchContract {
    fun onSearchQueryChange(newValue: TextFieldValue)
    fun onSearchFilterClick(query: String, category: SearchCategory)
    fun onCategorySelected(category: SearchCategory)
    fun onSavedMovieClick(movie: MovieUi)
    fun clearRecentViewed()
    fun clearRecentSearches()
    fun removeRecentSearch(search: RecentSearch)
    fun onRecentSearchClick(search: String)
    fun addToRecentSearches(query: RecentSearch)
    fun addToRecentViewed(item: RecentViewed)
    fun onClickMovie(genresListId : List<Int>)
    fun clearSearch()
    fun onApplyFilter(
        selectedGenres: List<Int>,
        minimumRating: Int,
        releaseYearRange: ClosedFloatingPointRange<Float>
    )
    fun onClearFilter()
    fun onReleaseYearRangeChange(range: ClosedFloatingPointRange<Float>)
    fun onGenreSelectedChange(selectedGenres: List<Int>)
    fun onRatingChanged(selectedRating: Int)
    fun onMovieClick(movieId: Int)
    fun onActorClick(actorId: Int)
    fun onTvShowClick(tvShowId: Int)
    fun onFilterClick()
    fun onFilterSheetDismiss()
    fun onRetry()
}
