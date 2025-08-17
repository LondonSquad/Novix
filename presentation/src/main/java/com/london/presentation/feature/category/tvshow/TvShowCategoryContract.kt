package com.london.presentation.feature.category.tvshow

interface TvShowCategoryContract {

    fun onBackClick()
    fun onSavedClick(tvShowId: Int)
    fun onTvShowClick(tvShowId: Int)
}
