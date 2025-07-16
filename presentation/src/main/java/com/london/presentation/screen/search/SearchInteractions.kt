package com.london.presentation.screen.search

import androidx.compose.ui.text.input.TextFieldValue
import com.london.presentation.screen.search.model.MovieUi

interface SearchInteractions {
    fun onSearchQueryChange(newValue: TextFieldValue)
    fun onSearchFilterClick(query: String, category: SearchCategory)
    fun onCategorySelected(category: SearchCategory)
    fun onSavedMovieClick(movie: MovieUi)
    fun clearRecentViewed()
    fun clearRecentSearches()
    fun removeRecentSearch(search: String)
    fun onRecentSearchClick(search: String)
    fun addToRecentSearches(query: String)
    fun addToRecentViewed(imageUrl: String)
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
}
