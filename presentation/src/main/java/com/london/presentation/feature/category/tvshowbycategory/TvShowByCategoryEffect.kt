package com.london.presentation.feature.category.tvshowbycategory

sealed interface TvShowByCategoryEffect {

    data class NavigateToTvShowDetails(val tvShowId: Int) : TvShowByCategoryEffect
    object NavigateBack : TvShowByCategoryEffect
}
