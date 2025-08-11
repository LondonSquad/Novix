package com.london.domain.usecase.movielist

import com.london.domain.entity.Movie
import com.london.domain.entity.MovieList
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.CustomMovieListRepository
import javax.inject.Inject

class ManageMovieListUseCase @Inject constructor(
    private val customMovieListRepository: CustomMovieListRepository,
) {

    suspend fun createMovieList(name: String): Boolean =
        customMovieListRepository.createMovieList(name)

    suspend fun deleteMovieList(id: UInt): Boolean = customMovieListRepository.deleteMovieList(id)

    suspend fun getMovieListDetails(listId: UInt, pageNumber: Int): PagedFetchResponse<Movie> =
        customMovieListRepository.getMovieListDetails(listId, pageNumber)

    suspend fun getMovieListName(listId: UInt): String =
        customMovieListRepository.getMovieListName(listId)

    suspend fun getMovieLists(pageNumber: Int): PagedFetchResponse<MovieList> =
        customMovieListRepository.getMovieLists(pageNumber = pageNumber)

    suspend fun addMovieToList(listId: UInt, movieId: UInt): Boolean =
        customMovieListRepository.addMovieToList(listId, movieId)

    suspend fun removeMovieFromList(listId: UInt, movieId: UInt): Boolean =
        customMovieListRepository.removeMovieFromList(listId, movieId)
}
