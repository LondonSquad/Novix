package com.london.data.repository.toprated

import com.london.data.local.model.home.topRated.TopRatedLocal
import com.london.data.local.source.home.HomeLocalDataSource
import com.london.data.mapper.toprated.toEntity
import com.london.data.mapper.toprated.toLocal
import com.london.data.mapper.toprated.toMovieEntity
import com.london.data.remote.source.toprated.movie.TopRatedMovieRemoteDataSource
import com.london.data.utils.CrashReporter
import com.london.data.utils.fetchAndSync
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.toprated.TopRatedMovie
import com.london.domain.repository.toprated.TopRatedMovieRepository
import javax.inject.Inject

class TopRatedMovieRepositoryImpl @Inject constructor(
    private val topRatedMovieRemoteDataSource: TopRatedMovieRemoteDataSource,
    private val localTopRated: HomeLocalDataSource<TopRatedLocal>,
    private val crashReporter: CrashReporter
) : TopRatedMovieRepository {
    override suspend fun getTopRatedMovies(
        pageNumber: Int,
    ): PagedFetchResponse<TopRatedMovie> = fetchAndSync(
        cacheBlock = {
            val local = localTopRated.getAll()
                .filter { it.mediaType == MediaType.Movie }
                .map { it.toMovieEntity() }
            local.takeIf { it.isNotEmpty() }
        },
        networkBlock = {
            topRatedMovieRemoteDataSource
                .getTopRatedMovies(pageNumber)
                .getOrThrow()
                .items.map { it.toEntity() }
        },
        syncBlock = { topRatedMovies ->
            localTopRated.insertAll(topRatedMovies.map { it.toLocal() })},
        crashReporter = crashReporter
    ).run {
        val remoteResult = topRatedMovieRemoteDataSource.getTopRatedMovies(pageNumber).getOrThrow()
        val movies = remoteResult.items.map { it.toEntity() }
        PagedFetchResponse(
            totalPages = remoteResult.totalPages,
            currentPage = remoteResult.currentPage,
            items = movies,
            totalItems = remoteResult.totalItems
        )
    }
}