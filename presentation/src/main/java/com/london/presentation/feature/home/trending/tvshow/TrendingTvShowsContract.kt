package com.london.presentation.feature.home.trending.tvshow

import com.london.presentation.shared.genre.TvShowGenreUi

interface TrendingTvShowsContract {
    fun onBackClick()
    fun onRetryClick()
    fun onTvShowClick(id: Int)
    fun onGenreClick(genre: TvShowGenreUi)
}

fun defaultTrendingTvShowsContract() = object : TrendingTvShowsContract {
    override fun onBackClick() {}
    override fun onRetryClick() {}
    override fun onTvShowClick(id: Int) {}
    override fun onGenreClick(genre: TvShowGenreUi) {}
}
