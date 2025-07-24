package com.london.domain.repository

import com.london.domain.entity.trending.TrendingMovie
import com.london.domain.entity.PagedFetchResponse


interface TrendingRepository {
    suspend fun getTrendingMovies(page: Int): PagedFetchResponse<TrendingMovie>
}