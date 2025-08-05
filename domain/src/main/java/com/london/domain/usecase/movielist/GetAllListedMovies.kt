package com.london.domain.usecase.movielist

import com.london.domain.entity.Movie
import com.london.domain.repository.CustomMovieListRepository
import javax.inject.Inject

class GetAllListedMovies @Inject constructor(
    private val customMovieListRepository: CustomMovieListRepository,
) {

    val allMoviesSet: Set<Movie> = mutableSetOf()

    suspend fun invoke(): Set<Movie> {

//        customMovieListRepository.getMovieLists(pageNumber = ).items.flatMap { movieList ->
//            customMovieListRepository.getMovieListDetails(movieList.id, pageNumber = ).items
//        }.toSet()

        val allMovies = mutableSetOf<Movie>()
        var listPageNumber = 1
        var hasMoreLists = true

        while (hasMoreLists) {
            val listsResponse = customMovieListRepository.getMovieLists(pageNumber = listPageNumber)

            listsResponse.items.forEach { movieList ->
                var moviePageNumber = 1
                var hasMoreMovies = true

                while (hasMoreMovies) {
                    val moviesResponse = customMovieListRepository.getMovieListDetails(
                        listId = movieList.id,
                        pageNumber = moviePageNumber
                    )
                    allMovies.addAll(moviesResponse.items)

                    hasMoreMovies = moviePageNumber < moviesResponse.totalPages
                    moviePageNumber++
                }
            }

            hasMoreLists = listPageNumber < listsResponse.totalPages
            listPageNumber++
        }

        return allMovies
    }
}
