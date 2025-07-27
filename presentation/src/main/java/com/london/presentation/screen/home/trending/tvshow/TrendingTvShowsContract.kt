package com.london.presentation.screen.home.trending.tvshow

import com.london.presentation.utils.TvShowGenre

interface TrendingTvShowsContract {
    fun onTvShowClick(id: Int)
    fun onBackClick()
    fun onGenreSelected(genre: TvShowGenre)
}

fun defaultTrendingTvShowsContract() = object : TrendingTvShowsContract {
    override fun onTvShowClick(id: Int) {}
    override fun onBackClick() {}
    override fun onGenreSelected(genre: TvShowGenre) {}
}