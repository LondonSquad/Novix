package com.london.data.repository.list

import com.london.data.mapper.search.toEntity
import com.london.data.remote.source.list.MovieCustomListsRemoteDataSource
import com.london.domain.entity.Movie
import com.london.domain.entity.MovieList
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.MovieListRepository

class MovieListRepositoryImpl(
    private val remoteDataSource: MovieCustomListsRemoteDataSource
) : MovieListRepository {

    override suspend fun deleteMovieList(id: UInt): Boolean =
        remoteDataSource.delete(id.toInt()).isSuccess

    override suspend fun createMovieList(name: String): Boolean =
        remoteDataSource.create(name).isSuccess

    override suspend fun getMovieLists(): PagedFetchResponse<MovieList> {
        TODO("Not yet implemented")
    }

    override suspend fun addMovieToList(listId: UInt, movieId: UInt): Boolean =
        remoteDataSource.addMovieToList(listId.toInt(), movieId.toInt()).isSuccess

    override suspend fun getMovieListDetails(listId: UInt): PagedFetchResponse<Movie> {
        val response = remoteDataSource.getDetails(listId.toInt(), 1).getOrThrow()
        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toEntity() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }
}
