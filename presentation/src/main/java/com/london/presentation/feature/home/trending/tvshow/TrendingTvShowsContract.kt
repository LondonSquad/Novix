package com.london.presentation.feature.home.trending.tvshow

import com.london.domain.entity.genre.TvShowGenre
import com.london.presentation.shared.genre.TvShowGenreUi

interface TrendingTvShowsContract {
    fun onBack()
    fun onRetry()
    fun onTvShowClick(id: Int)
    fun onGenreSelected(genre: TvShowGenreUi)
}

fun defaultTrendingTvShowsContract() = object : TrendingTvShowsContract {
    override fun onBack() {}
    override fun onRetry() {}
    override fun onTvShowClick(id: Int) {}
    override fun onGenreSelected(genre: TvShowGenreUi) {}
}
