package com.london.data.repository

import com.london.data.datasource.remote.toprated.tvseries.TopRatedTvRemoteDataSource
import com.london.data.mapper.toprated.toEntity
import com.london.domain.entity.toprated.TopRatedTvSeries
import com.london.domain.repository.TopRatedTvSeriesRepo
import org.koin.core.annotation.Single

@Single
class TopRatedTvSeriesRepoImpl(
    private val topRatedTvRemoteDataSource: TopRatedTvRemoteDataSource
) : TopRatedTvSeriesRepo {
    override suspend fun getTopRatedTvSeries(
        pageNumber: Int,
        language: String
    ): List<TopRatedTvSeries> =
        topRatedTvRemoteDataSource.getTopRatedTvShows(
            pageNumber,
            language
        ).items.map { it.toEntity() }
}