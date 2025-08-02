package com.london.domain.repository

interface AddMovieRatingRepository {
    suspend fun addMovieRatingById(
        id: Int,
        rating: Double,
    ) : Boolean
}