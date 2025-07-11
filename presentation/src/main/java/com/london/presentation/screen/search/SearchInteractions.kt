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
}