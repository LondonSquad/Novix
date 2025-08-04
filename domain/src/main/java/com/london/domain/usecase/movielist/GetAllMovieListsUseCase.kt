package com.london.domain.usecase.movielist

import com.london.domain.entity.MovieList
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.MovieListRepository
import javax.inject.Inject

class GetAllMovieListsUseCase @Inject constructor(
    private val movieListRepository: MovieListRepository,
) {

    suspend fun invoke(): PagedFetchResponse<MovieList> =
        movieListRepository.getMovieLists()
}
