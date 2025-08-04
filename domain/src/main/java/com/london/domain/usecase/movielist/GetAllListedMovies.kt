package com.london.domain.usecase.movielist

import com.london.domain.entity.Movie
import com.london.domain.repository.MovieListRepository
import javax.inject.Inject

class GetAllListedMovies @Inject constructor(
    private val movieListRepository: MovieListRepository,
) {

    suspend fun invoke(): Set<Movie> =
        movieListRepository.getMovieLists().items.flatMap { movieList ->
            movieListRepository.getMovieListDetails(movieList.id).items
        }.toSet()
}
