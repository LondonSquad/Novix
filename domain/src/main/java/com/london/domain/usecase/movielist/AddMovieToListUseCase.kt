package com.london.domain.usecase.movielist

import com.london.domain.repository.MovieListRepository
import javax.inject.Inject

class AddMovieToListUseCase @Inject constructor(
    private val movieListRepository: MovieListRepository,
) {

    suspend fun invoke(listId: UInt, movieId: UInt): Boolean =
        movieListRepository.addMovieToList(listId, movieId)
}
