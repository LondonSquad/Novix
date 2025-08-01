package com.london.data.repository.toprated

import com.london.data.mapper.toprated.toEntity
import com.london.data.remote.source.toprated.movie.TopRatedMovieRemoteDataSource
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.toprated.TopRatedMovie
import com.london.domain.repository.toprated.TopRatedMovieRepository
import javax.inject.Inject

class TopRatedMovieRepositoryImpl @Inject constructor(
    private val topRatedMovieRemoteDataSource: TopRatedMovieRemoteDataSource
) : TopRatedMovieRepository {
    override suspend fun getTopRatedMovies(
        pageNumber: Int,
    ): PagedFetchResponse<TopRatedMovie> {
        val remoteResult = topRatedMovieRemoteDataSource
            .getTopRatedMovies(pageNumber).getOrThrow()

        val movies = remoteResult.items.map { it.toEntity() }
        return PagedFetchResponse(
            totalPages = remoteResult.totalPages,
            currentPage = remoteResult.currentPage,
            items = movies,
            totalItems = remoteResult.totalItems
        )
    }
}