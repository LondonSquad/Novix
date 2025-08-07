package com.london.domain.usecase

import com.london.domain.repository.RatingRepository

class AddTvEpisodeByIdUseCase(
    private val ratingRepository: RatingRepository
) {
    suspend fun invoke(id: Int, rating: Int) =
        ratingRepository.addMovieRatingById(
            id = id,
            rating = rating
        )
}