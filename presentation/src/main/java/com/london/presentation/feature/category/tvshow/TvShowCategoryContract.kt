package com.london.presentation.feature.category.tvshow

interface TvShowCategoryContract {
    fun onBack()
    fun onSavedClick(tvShowId: Int)
    fun onTvShowClick(tvShowId: Int)
}
