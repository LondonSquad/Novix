package com.london.presentation.feature.category.tvshow

sealed interface TvShowCategoryEffect {

    data object NavigateBack : TvShowCategoryEffect
    data class NavigateToTvShowDetails(val tvShowId: Int) : TvShowCategoryEffect
}
