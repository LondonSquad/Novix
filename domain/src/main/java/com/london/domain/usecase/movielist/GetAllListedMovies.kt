package com.london.domain.usecase.movielist

import com.london.domain.entity.Movie
import com.london.domain.entity.MovieList
import com.london.domain.repository.CustomMovieListRepository
import javax.inject.Inject

typealias MovieId = UInt
typealias ListId = UInt
typealias MovieToListsMap = Map<MovieId, Set<ListId>>

class GetAllListedMovies @Inject constructor(
    private val customMovieListRepository: CustomMovieListRepository,
) {

    suspend fun invoke(): MovieToListsMap {
        val allLists = getAllLists()
        val movieToListsMap = mutableMapOf<MovieId, MutableSet<ListId>>()

        allLists.forEach { movieList ->
            val moviesInList = getAllMovies(movieList.id)
            moviesInList.forEach { movie ->
                movieToListsMap.getOrPut(movie.id.toUInt()) { mutableSetOf() }.add(movieList.id)
            }
        }

        return movieToListsMap
    }

    suspend fun getAllUniqueMovies(): Set<Movie> {
        return getAllLists().flatMap { movieList ->
            getAllMovies(movieList.id)
        }.toSet()
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

    private suspend fun getAllMovies(listId: ListId): List<Movie> {
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
