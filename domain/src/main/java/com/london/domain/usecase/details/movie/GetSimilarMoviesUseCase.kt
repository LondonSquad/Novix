package com.london.domain.usecase.details.movie

import com.london.domain.repository.MovieDetailsRepository
import javax.inject.Inject


class GetSimilarMoviesUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) {
    suspend fun invoke(movieId: Int) = movieDetailsRepository.getSimilarMoviesById(movieId)
}