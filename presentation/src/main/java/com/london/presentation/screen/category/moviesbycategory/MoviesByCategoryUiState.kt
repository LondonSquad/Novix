package com.london.presentation.screen.category.moviesbycategory

import com.london.domain.entity.Movie

data class MoviesByCategoryUiState(
    val movies: List<Movie> = listOf(),
    val categoryName: String = "",
)
