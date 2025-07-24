package com.london.domain.repository

import com.london.domain.entity.toprated.TopRatedTvSeries

interface TopRatedTvSeriesRepository {
    suspend fun getTopRatedTvSeries(
        pageNumber: Int,
        language: String,
    ): List<TopRatedTvSeries>
}