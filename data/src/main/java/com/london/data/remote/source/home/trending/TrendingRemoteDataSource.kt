package com.london.data.remote.source.home.trending

import com.london.data.remote.model.trending.TrendingRemote

interface TrendingRemoteDataSource {
    suspend fun getTrendingMovies(page: Int): TrendingRemote
    suspend fun getTrendingTvShows(page: Int): TrendingRemote
    suspend fun getTrendingActors(page: Int): TrendingRemote
}