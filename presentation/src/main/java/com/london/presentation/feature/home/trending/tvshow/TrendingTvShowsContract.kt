package com.london.presentation.feature.home.trending.tvshow

import com.london.presentation.shared.genre.TvShowGenreUi

interface TrendingTvShowsContract {
    fun onBackClick()
    fun onRetryClick()
    fun onTvShowClick(id: Int)
    fun onGenreClick(genre: TvShowGenreUi)
}
