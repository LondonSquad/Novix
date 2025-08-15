package com.london.presentation.feature.category.main

import com.london.presentation.shared.MediaCategory
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.TvShowGenreUi


data class CategoriesUiState(

    val selectedCategory: MediaCategory = MediaCategory.Movies,
    val movieGenres: List<MovieGenreUi> = MovieGenreUi.getListWithoutAll(),
    val tvShowGenres: List<TvShowGenreUi> = TvShowGenreUi.getListWithoutAll(),
)
