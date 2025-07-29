package com.london.presentation.feature.category.tvshowbycategory

interface TvShowByCategoryContract {

    fun onSavedClick(tvShowId: Int)
    fun onTvShowClick(tvShowId: Int)
    fun onBackClick()
}
