package com.london.domain.usecase.movielist

import com.london.domain.entity.MovieList
import com.london.domain.repository.MovieDetailsRepository
import javax.inject.Inject

class GetMovieListsUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) {

    suspend fun invoke(listId: UInt): List<MovieList> =
        movieDetailsRepository.getMovieLists(listId)
}