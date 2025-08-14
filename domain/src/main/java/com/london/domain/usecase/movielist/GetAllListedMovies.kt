package com.london.domain.usecase.movielist

import com.london.domain.entity.Movie
import com.london.domain.entity.MovieList
import com.london.domain.repository.CustomMovieListRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetAllListedMovies @Inject constructor(
    private val customMovieListRepository: CustomMovieListRepository,
) {

    suspend fun invoke(): Set<Movie> = coroutineScope {
        val allLists = getAllLists()

        allLists.map { movieList ->
            async { getAllMoviesFromList(movieList.id) }
        }.awaitAll().flatten().toSet()
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

    private suspend fun getAllMoviesFromList(listId: UInt): List<Movie> {
        val allMovies = mutableListOf<Movie>()
        var currentPage = 1

        do {
            val response = customMovieListRepository.getMovieListDetails(listId, currentPage)
            allMovies.addAll(response.items)
            currentPage++
        } while (currentPage <= response.totalPages)

        return allMovies
    }
}