package com.london.data.repository.toprated

import com.london.data.mapper.toprated.toEntity
import com.london.data.remote.source.toprated.tvseries.TopRatedTvRemoteDataSource
import com.london.data.utils.fetchAndSync
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.toprated.TopRatedTvSeries
import com.london.domain.repository.toprated.TopRatedTvSeriesRepository
import org.koin.core.annotation.Single

@Single
class TopRatedTvSeriesRepositoryImpl(
    private val topRatedTvRemoteDataSource: TopRatedTvRemoteDataSource
) : TopRatedTvSeriesRepository {
    override suspend fun getTopRatedTvSeries(
        pageNumber: Int
    ): PagedFetchResponse<TopRatedTvSeries> = fetchAndSync(
        networkBlock = {
            val remoteResult = topRatedTvRemoteDataSource.getTopRatedTvShows(
                pageNumber = pageNumber
            ).getOrThrow()

            PagedFetchResponse(
                totalPages = remoteResult.totalPages,
                items = remoteResult.items.map { it.toEntity() },
                currentPage = remoteResult.currentPage,
                totalItems = remoteResult.totalItems,
            )
        }
    )
}