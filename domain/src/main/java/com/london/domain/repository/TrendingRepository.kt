package com.london.domain.repository

import com.london.domain.entity.trending.Trending
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Actor


interface TrendingRepository {
    suspend fun getTrendingMovies(page: Int): PagedFetchResponse<Trending>
    suspend fun getTrendingTvShows(page: Int): PagedFetchResponse<Trending>
    suspend fun getTrendingActors(page: Int): PagedFetchResponse<Actor>
}