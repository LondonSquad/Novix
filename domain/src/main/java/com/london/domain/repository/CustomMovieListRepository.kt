package com.london.domain.repository

import com.london.domain.entity.movie.Movie
import com.london.domain.entity.movie.MovieList
import com.london.domain.entity.shared.PagedFetchResponse
import kotlinx.coroutines.flow.Flow

interface CustomMovieListRepository {

    suspend fun createMovieList(name: String): Boolean
    suspend fun deleteMovieList(id: Int): Boolean
    suspend fun getAllListedMovieIds(): List<Int>
    fun getAllListedMovieIdsFlow(): Flow<List<Int>>
    suspend fun addMovieToList(listId: Int, movieId: Int): Boolean
    suspend fun removeMovieFromList(listId: Int, movieId: Int): Boolean
    suspend fun getMovieListName(listId: Int): String
    suspend fun getMovieLists(pageNumber: Int): PagedFetchResponse<MovieList>
    suspend fun getMovieListDetails(listId: Int, pageNumber: Int): PagedFetchResponse<Movie>
    suspend fun isMovieListed(movieId: Int, forceRefresh: Boolean = false): Boolean
    fun isMovieListedFlow(movieId: Int): Flow<Boolean>
    suspend fun getMovieListIds(movieId: Int, forceRefresh: Boolean = false): List<Int>
    fun getMovieListIdsFlow(movieId: Int): Flow<List<Int>>
    suspend fun refreshMovieListCache()

}
