package com.london.domain.usecase.details.movie

import com.london.domain.repository.MovieDetailsRepository
import javax.inject.Inject

class GetMovieImagesUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) {
    suspend fun invoke(movieId: Int) =
        movieDetailsRepository.getMovieImagesById(movieId).take(IMAGE_LIMIT)

    companion object {
        private const val IMAGE_LIMIT = 10
    }
}
