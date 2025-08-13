package com.london.presentation.feature.category.tvshow

sealed interface TvShowCategoryEffect {

    data object NavigationBack : TvShowCategoryEffect
    data class TvShowDetailsNavigation(val tvShowId: Int) : TvShowCategoryEffect
}
