package com.london.domain.repository

import com.london.domain.entity.Movie
import com.london.domain.entity.MovieList
import com.london.domain.entity.PagedFetchResponse

interface CustomMovieListRepository {

    suspend fun deleteMovieList(id: UInt): Boolean
    suspend fun createMovieList(name: String): Boolean
    suspend fun getMovieLists(pageNumber: Int): PagedFetchResponse<MovieList>
    suspend fun addMovieToList(listId: UInt, movieId: UInt): Boolean
    suspend fun getMovieListDetails(listId: UInt, pageNumber: Int): PagedFetchResponse<Movie>
}
