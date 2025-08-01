package com.london.domain.usecase.details.movie

import com.london.domain.repository.MovieVideoProviderRepository
import javax.inject.Inject

class GetMovieVideoUseCase @Inject constructor(
    private val providerRepository: MovieVideoProviderRepository
) {
    suspend fun invoke(movieId: Int) = providerRepository.getMovieVideos(movieId)
}
