package com.london.domain.usecase.movielist

import com.london.domain.entity.Movie
import com.london.domain.entity.MovieList
import com.london.domain.repository.CustomMovieListRepository
import javax.inject.Inject

class GetAllListedMovies @Inject constructor(
    private val customMovieListRepository: CustomMovieListRepository,
) {
    suspend fun invoke(): Set<Movie> =
        getAllLists(page = 1).flatMap { movieList ->
            getAllMovies(movieList.id, page =  1)
        }.toSet()

    private suspend fun getAllLists(page: Int): List<MovieList> =
        customMovieListRepository.getMovieLists(page).let { response ->
            val nextPages =
                if (page < response.totalPages) getAllLists(page + 1)
                else emptyList()
            response.items + nextPages
        }

    private suspend fun getAllMovies(listId: UInt, page: Int): List<Movie> =
        customMovieListRepository.getMovieListDetails(listId, page).let { response ->
            val nextPages =
                if (page < response.totalPages) getAllMovies(listId, page + 1)
                else emptyList()
            response.items + nextPages
        }
}
