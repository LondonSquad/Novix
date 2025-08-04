package com.london.data.repository.discover

import com.london.data.mapper.toEntity
import com.london.data.remote.source.discover.DiscoverRemoteDataSource
import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.TvShow
import com.london.domain.repository.discover.DiscoverRepository
import javax.inject.Inject

class DiscoverRepositoryImpl @Inject constructor(
    private val remoteDataSource: DiscoverRemoteDataSource,
) : DiscoverRepository {

    override suspend fun getMoviesByCategory(
        categoryId: Int,
        pageNumber: Int
    ): PagedFetchResponse<Movie> {
        val response = remoteDataSource.getMoviesByCategory(categoryId, pageNumber).getOrThrow()
        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toEntity() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }

    override suspend fun getTvShowsByCategory(
        categoryId: Int,
        pageNumber: Int
    ): PagedFetchResponse<TvShow> {
        val response = remoteDataSource.getTvShowsByCategoryId(categoryId, pageNumber).getOrThrow()
        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toEntity() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }

}
