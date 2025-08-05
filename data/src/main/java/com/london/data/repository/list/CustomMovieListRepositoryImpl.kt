package com.london.data.repository.list

import com.london.data.mapper.list.toEntity
import com.london.data.mapper.search.toEntity
import com.london.data.remote.source.list.CustomMovieListsRemoteDataSource
import com.london.domain.entity.Movie
import com.london.domain.entity.MovieList
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.CustomMovieListRepository

class CustomMovieListRepositoryImpl(
    private val remoteDataSource: CustomMovieListsRemoteDataSource
) : CustomMovieListRepository {

    override suspend fun deleteMovieList(id: UInt): Boolean =
        remoteDataSource.delete(id.toInt()).isSuccess

    override suspend fun createMovieList(name: String): Boolean =
        remoteDataSource.create(name).isSuccess

    override suspend fun getMovieLists(pageNumber: Int): PagedFetchResponse<MovieList> {
        val response = remoteDataSource.getAllMovieLists(pageNumber).getOrThrow()

        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toEntity() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }

    override suspend fun addMovieToList(listId: UInt, movieId: UInt): Boolean =
        remoteDataSource.addMovieToList(listId.toInt(), movieId.toInt()).isSuccess

    override suspend fun getMovieListDetails(
        listId: UInt,
        pageNumber: Int
    ): PagedFetchResponse<Movie> {
        val response = remoteDataSource.getDetails(listId.toInt(), pageNumber).getOrThrow()
        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toEntity() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }
}
