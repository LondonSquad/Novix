package com.london.domain.repository

interface RatingRepository {
    suspend fun addMovieRatingById(
        id: Int,
        rating: Int,
    ) : Boolean
}