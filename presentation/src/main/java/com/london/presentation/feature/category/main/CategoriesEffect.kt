package com.london.presentation.feature.category.main

import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

interface CategoriesEffect {

    data class MovieCategoryNavigation(val movieGenre: MovieGenre) : CategoriesEffect
    data class TvShowCategoryNavigation(val tvShowGenre: TvShowGenre) : CategoriesEffect
}
