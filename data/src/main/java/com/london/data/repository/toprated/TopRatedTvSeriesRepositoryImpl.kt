package com.london.data.repository.toprated

import com.london.data.local.model.home.TopRatedLocal
import com.london.data.local.source.home.popular.HomeLocalDataSource
import com.london.data.mapper.toprated.toEntity
import com.london.data.mapper.toprated.toLocal
import com.london.data.mapper.toprated.toTvShow
import com.london.data.remote.source.toprated.tvseries.TopRatedTvRemoteDataSource
import com.london.data.utils.CrashReporter
import com.london.data.utils.fetchAndSync
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.toprated.TopRatedTvSeries
import com.london.domain.repository.toprated.TopRatedTvSeriesRepository
import javax.inject.Inject

class TopRatedTvSeriesRepositoryImpl @Inject constructor(
    private val topRatedTvRemoteDataSource: TopRatedTvRemoteDataSource,
    private val topRatedTvShow: HomeLocalDataSource<TopRatedLocal>,
    private val crashReporter: CrashReporter
) : TopRatedTvSeriesRepository {
    override suspend fun getTopRatedTvSeries(
        pageNumber: Int
    ): PagedFetchResponse<TopRatedTvSeries> = fetchAndSync(
        cacheBlock = {
            val local = topRatedTvShow.getAll()
                .filter { it.mediaType == MediaType.TvShow }
                .map { it.toTvShow() }
            local.takeIf { it.isNotEmpty() }
        },
        networkBlock = {
            topRatedTvRemoteDataSource
                .getTopRatedTvShows(pageNumber = pageNumber)
                .getOrThrow()
                .items.map { it.toEntity() }
        },
        syncBlock = { topRatedTvShows ->
            topRatedTvShow.insertAll(topRatedTvShows.map { it.toLocal() })
        },
        crashReporter = crashReporter
    )
        .run {
            val remoteResult =
                topRatedTvRemoteDataSource.getTopRatedTvShows(pageNumber = pageNumber).getOrThrow()
            PagedFetchResponse(
                totalPages = remoteResult.totalPages,
                items = remoteResult.items.map { it.toEntity() },
                currentPage = remoteResult.currentPage,
                totalItems = remoteResult.totalItems,
            )
        }
}