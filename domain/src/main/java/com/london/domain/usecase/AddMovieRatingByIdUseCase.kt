package com.london.domain.usecase

import com.london.domain.repository.RatingRepository
import javax.inject.Inject

class AddMovieRatingByIdUseCase @Inject constructor(
    private val repository: RatingRepository
) {
    suspend operator fun invoke(
        id: Int,
        rating: Int,
    ): Boolean = repository.addMovieRatingById(
            id = id,
            rating = rating,
        )
}