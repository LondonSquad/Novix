package com.london.presentation.feature.category.main

import com.london.presentation.shared.MediaCategory
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

data class CategoriesUiState(

    val selectedCategory: MediaCategory = MediaCategory.Movies,
    val movieGenres: List<MovieGenre> = MovieGenre.entries.filter { it != MovieGenre.All },
    val tvShowGenres: List<TvShowGenre> = TvShowGenre.entries.filter { it != TvShowGenre.All },
)
