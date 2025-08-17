package com.london.presentation.feature.category.tvshow

sealed interface TvShowCategoryEffect {
    data object BackNavigation : TvShowCategoryEffect
    data class TvShowDetailsNavigation(val tvShowId: Int) : TvShowCategoryEffect
}
