package com.london.domain.usecase

import com.london.domain.repository.RatingRepository
import javax.inject.Inject

class AddTvEpisodeRatingByIdUseCase @Inject constructor(
    private val ratingRepository: RatingRepository
) {
    suspend fun invoke(id: Int, rating: Int) =
        ratingRepository.addMovieRatingById(
            id = id,
            rating = rating
        )
}