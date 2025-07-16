package com.london.domain.usecase

import com.london.domain.repository.MovieDetailsRepository

class GetMovieCastUseCase(
    private val movieDetailsRepository: MovieDetailsRepository
) {
    suspend fun invoke(movieId: Int) = movieDetailsRepository.getMovieCastById(movieId)
}