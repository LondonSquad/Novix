package com.london.presentation.feature.home.trending.tvshow

import com.london.presentation.utils.TvShowGenre

interface TrendingTvShowsContract {
    fun onBackClick()
    fun onRetryClick()
    fun onTvShowClick(id: Int)
    fun onGenreClick(genre: TvShowGenre)
}

fun defaultTrendingTvShowsContract() = object : TrendingTvShowsContract {
    override fun onBackClick() {}
    override fun onRetryClick() {}
    override fun onTvShowClick(id: Int) {}
    override fun onGenreClick(genre: TvShowGenre) {}
}
