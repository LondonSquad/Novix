package com.london.data.repository.home.toprated

import com.london.data.local.model.home.topRated.TopRatedLocal
import com.london.data.local.source.home.HomeLocalDataSource
import com.london.data.mapper.home.toprated.toEntity
import com.london.data.mapper.home.toprated.toLocal
import com.london.data.remote.source.toprated.TopRatedRemoteDataSource
import com.london.data.utils.CrashReporter
import com.london.data.utils.fetchAndSync
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.toprated.TopRatedMedia
import com.london.domain.repository.toprated.TopRatedRepository
import javax.inject.Inject

class TopRatedRepositoryImpl @Inject constructor(
    private val topRatedRemoteDataSource: TopRatedRemoteDataSource,
    private val localTopRated: HomeLocalDataSource<TopRatedLocal>,
    private val crashReporter: CrashReporter
) : TopRatedRepository {

    override suspend fun getTopRatedMovies(
        pageNumber: Int,
    ): PagedFetchResponse<TopRatedMedia> = fetchAndSync(
        cacheBlock = {
            val local = localTopRated.getAll()
                .filter { it.mediaType == MediaType.Movie }
                .map { it.toEntity() }
            local.takeIf { it.isNotEmpty() }
        },
        networkBlock = {
            topRatedRemoteDataSource
                .getTopRatedMovies(pageNumber)
                .getOrThrow()
                .items.map { it.toEntity() }
        },
        syncBlock = { topRatedMovies ->
            localTopRated.insertAll(topRatedMovies.map { it.toLocal() })},
        crashReporter = crashReporter
    ).run {
        val remoteResult = topRatedRemoteDataSource.getTopRatedMovies(pageNumber).getOrThrow()
        val movies = remoteResult.items.map { it.toEntity() }
        PagedFetchResponse(
            totalPages = remoteResult.totalPages,
            currentPage = remoteResult.currentPage,
            items = movies,
            totalItems = remoteResult.totalItems
        )
    }

    override suspend fun getTopRatedTvSeries(
        pageNumber: Int
    ): PagedFetchResponse<TopRatedMedia> = fetchAndSync(
        cacheBlock = {
            val local = localTopRated.getAll()
                .filter { it.mediaType == MediaType.TvShow }
                .map { it.toEntity() }
            local.takeIf { it.isNotEmpty() }
        },
        networkBlock = {
            topRatedRemoteDataSource
                .getTopRatedTvShows(pageNumber = pageNumber)
                .getOrThrow()
                .items.map { it.toEntity() }
        },
        syncBlock = { topRatedTvShows ->
            localTopRated.insertAll(topRatedTvShows.map { it.toLocal() })
        },
        crashReporter = crashReporter
    )
        .run {
            val remoteResult =
                topRatedRemoteDataSource.getTopRatedTvShows(pageNumber = pageNumber).getOrThrow()
            PagedFetchResponse(
                totalPages = remoteResult.totalPages,
                items = remoteResult.items.map { it.toEntity() },
                currentPage = remoteResult.currentPage,
                totalItems = remoteResult.totalItems,
            )
        }
}