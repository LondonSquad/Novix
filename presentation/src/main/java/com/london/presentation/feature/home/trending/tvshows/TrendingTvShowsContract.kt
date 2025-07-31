package com.london.presentation.feature.home.trending.tvshows

import com.london.presentation.utils.TvShowGenre

interface TrendingTvShowsContract {
    fun onTvShowClick(id: Int)
    fun onGenreSelected(genre: TvShowGenre)
    fun onBack()
    fun onRetry()
}

fun defaultTrendingTvShowsContract() = object : TrendingTvShowsContract {
    override fun onTvShowClick(id: Int) {}
    override fun onGenreSelected(genre: TvShowGenre) {}
    override fun onBack() {}
    override fun onRetry() {}
}
