package com.london.domain.usecase.movielist

import com.london.domain.entity.MovieList
import com.london.domain.repository.CustomMovieListRepository
import com.london.domain.repository.MovieDetailsRepository
import javax.inject.Inject

class GetAvailableListsForMovie @Inject constructor(
    private val customMovieListRepository: CustomMovieListRepository,
    private val movieDetailsRepository: MovieDetailsRepository
) {

    suspend fun invoke(movieId: UInt): List<MovieList> {
        val allLists = getAllLists()
        val movieLists = getMovieLists(movieId = movieId)

        return allLists.filterNot { movieList ->
            movieLists.any { it.id == movieList.id }
        }
    }

    private suspend fun getMovieLists(movieId: UInt): List<MovieList> {
        return movieDetailsRepository.getMovieLists(movieId = movieId)
    }

    private suspend fun getAllLists(): List<MovieList> {
        val allLists = mutableListOf<MovieList>()
        var currentPage = 1

        do {
            val response = customMovieListRepository.getMovieLists(currentPage)
            allLists.addAll(response.items)
            currentPage++
        } while (currentPage <= response.totalPages)

        return allLists
    }

}