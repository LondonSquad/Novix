package com.london.presentation.feature.category.tvshowbycategory

interface TvShowByCategoryContract {

    fun onBack()
    fun onSavedClick(tvShowId: Int)
    fun onTvShowClick(tvShowId: Int)
}
