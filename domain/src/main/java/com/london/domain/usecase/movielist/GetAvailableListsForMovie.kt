package com.london.domain.usecase.movielist

import com.london.domain.entity.movie.MovieList
import com.london.domain.repository.CustomMovieListRepository
import javax.inject.Inject

class GetAvailableListsForMovie @Inject constructor(
    private val repository: CustomMovieListRepository
) {

    suspend fun invoke(movieId: Int): List<MovieList> {
        val allLists = getAllLists()
        val movieLists = repository.getMovieListIds(movieId)

        return allLists.filterNot { movieList ->
            movieLists.any { it == movieList.id }
        }
    }

    private suspend fun getAllLists(): List<MovieList> {
        val allLists = mutableListOf<MovieList>()
        var currentPage = 1

        do {
            val response = repository.getMovieLists(currentPage)
            allLists.addAll(response.items)
            currentPage++
        } while (currentPage <= response.totalPages)

        return allLists
    }

}
