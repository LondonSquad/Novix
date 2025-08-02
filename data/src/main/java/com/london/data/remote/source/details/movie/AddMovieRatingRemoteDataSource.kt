package com.london.data.remote.source.details.movie

import com.london.data.remote.model.details.RatingResponse

interface AddMovieRatingRemoteDataSource {
    suspend fun addMovieRating(
        movieId: Int,
        rating: Double,
        guestSessionId: String?,
        userSessionId: String?
    ): Result<RatingResponse>
}