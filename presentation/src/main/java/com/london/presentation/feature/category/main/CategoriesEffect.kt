package com.london.presentation.feature.category.main

import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.TvShowGenreUi


interface CategoriesEffect {

    data class MovieCategoryNavigation(val movieGenre: MovieGenreUi) : CategoriesEffect
    data class TvShowCategoryNavigation(val tvShowGenre: TvShowGenreUi) : CategoriesEffect
}
