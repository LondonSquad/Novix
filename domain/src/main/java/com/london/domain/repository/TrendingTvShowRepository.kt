package com.london.domain.repository

import com.london.domain.entity.trending.TrendingTvShow
import com.london.domain.entity.PagedFetchResponse

interface TrendingTvShowRepository {
    suspend fun getTrendingTvShows(page: Int): PagedFetchResponse<TrendingTvShow>
} 