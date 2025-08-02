package com.london.domain.usecase

import com.london.domain.repository.AddMovieRatingRepository
import javax.inject.Inject

class AddMovieRatingByIdUseCase @Inject constructor(
    private val repository: AddMovieRatingRepository
) {
    suspend operator fun invoke(
        id: Int,
        rating: Double,
    ): Boolean = repository.addMovieRatingById(
            id = id,
            rating = rating,
        )
}