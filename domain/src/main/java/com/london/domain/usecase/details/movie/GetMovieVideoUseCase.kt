package com.london.domain.usecase.details.movie

import com.london.domain.repository.MovieVideoProviderRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetMovieVideoUseCase(
    @Provided
    private val providerRepository: MovieVideoProviderRepository
) {
    suspend fun invoke(movieId: Int) = providerRepository.getMovieVideos(movieId)
}
