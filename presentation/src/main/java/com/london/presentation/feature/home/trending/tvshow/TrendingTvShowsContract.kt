package com.london.presentation.feature.home.trending.tvshow

import com.london.presentation.utils.TvShowGenre

interface TrendingTvShowsContract {
    fun onBack()
    fun onRetry()
    fun onTvShowClick(id: Int)
    fun onGenreSelected(genre: TvShowGenre)
}

fun defaultTrendingTvShowsContract() = object : TrendingTvShowsContract {
    override fun onBack() {}
    override fun onRetry() {}
    override fun onTvShowClick(id: Int) {}
    override fun onGenreSelected(genre: TvShowGenre) {}
}
