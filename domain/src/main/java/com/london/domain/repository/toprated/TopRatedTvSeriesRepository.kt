package com.london.domain.repository.toprated

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.toprated.TopRatedTvSeries

interface TopRatedTvSeriesRepository {
    suspend fun getTopRatedTvSeries(
        pageNumber: Int,
    ): PagedFetchResponse<TopRatedTvSeries>
}