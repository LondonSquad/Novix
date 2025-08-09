package com.london.presentation.feature.category.tvshowbycategory

sealed interface TvShowByCategoryEffect {

    data object NavigateBack : TvShowByCategoryEffect
    data class NavigateToTvShowDetails(val tvShowId: Int) : TvShowByCategoryEffect
}
