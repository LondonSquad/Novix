package com.london.presentation.screen.search

import com.london.presentation.screen.search.model.MovieUi

interface SearchInteractions {
    fun onSearchQueryChange(query: String)
    fun onSearchFilterClick()
    fun onCategorySelected(category: SearchCategory)
    fun onSavedMovieClick(movie: MovieUi)
}