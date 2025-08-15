package com.london.presentation.feature.category.main

interface CategoriesEffect {
    data class MovieCategoryNavigation(val genreId: Int) : CategoriesEffect
    data class TvShowCategoryNavigation(val genreId: Int) : CategoriesEffect
}
