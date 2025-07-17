package com.london.domain.usecase

import com.london.domain.repository.MovieDetailsRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetMovieById(
    @Provided
    private val movieRepository: MovieDetailsRepository
) {
    suspend fun invoke(movieId: Int) = movieRepository.getMovieById(movieId)
}