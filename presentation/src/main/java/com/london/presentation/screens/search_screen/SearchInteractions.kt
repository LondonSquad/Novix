package com.london.presentation.screens.search_screen

import com.london.presentation.screens.search_screen.model.MovieUi

interface SearchInteractions {
    fun onSearchQueryChange(query: String)
    fun onSearchFilterClick()
    fun onCategorySelected(category: SearchCategory)
    fun onSavedMovieClick(movie: MovieUi)
}