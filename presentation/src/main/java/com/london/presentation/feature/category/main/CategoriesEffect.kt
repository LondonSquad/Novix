package com.london.presentation.feature.category.main

import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

interface CategoriesEffect {

    data class NavigateToMovieCategory(val movieGenre: MovieGenre) : CategoriesEffect
    data class NavigateToTvShowCategory(val tvShowGenre: TvShowGenre) : CategoriesEffect
}
