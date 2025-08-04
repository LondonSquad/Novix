package com.london.domain.usecase.movielist

import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.MovieListRepository
import javax.inject.Inject

class GetMovieListDetailsUseCase @Inject constructor(
    private val movieListRepository: MovieListRepository,
) {

    suspend fun invoke(listId: UInt): PagedFetchResponse<Movie> =
        movieListRepository.getMovieListDetails(listId)
}
