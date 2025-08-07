package com.london.domain.usecase

import com.london.domain.repository.RatingRepository

class AddTvShowRatingByIdUseCase(
    private val ratingRepository: RatingRepository
) {
    suspend fun invoke(id: Int, rating: Int) =
        ratingRepository.addTvShowById(
            id = id,
            rating = rating,
        )
}