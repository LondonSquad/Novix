package com.london.data.repository

import com.london.data.remote.source.toprated.tvseries.TopRatedTvRemoteDataSource
import com.london.data.mapper.toprated.toEntity
import com.london.domain.entity.toprated.TopRatedTvSeries
import com.london.domain.repository.TopRatedTvSeriesRepository
import org.koin.core.annotation.Single

@Single
class TopRatedTvSeriesRepoImpl(
    private val topRatedTvRemoteDataSource: TopRatedTvRemoteDataSource
) : TopRatedTvSeriesRepository {
    override suspend fun getTopRatedTvSeries(
        pageNumber: Int, language: String
    ): List<TopRatedTvSeries> = topRatedTvRemoteDataSource.getTopRatedTvShows(
        pageNumber, language
    ).getOrThrow().items.map { it.toEntity() }
}