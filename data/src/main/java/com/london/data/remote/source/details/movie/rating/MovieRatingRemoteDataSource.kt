package com.london.data.remote.source.details.movie.rating

import com.london.data.remote.model.details.rating.RatingRemoteResponse


interface MovieRatingRemoteDataSource {
    suspend fun addMovieRating(
        movieId: Int,
        rating: Double,
        guestSessionId: String?,
        userSessionId: String?
    ): Result<RatingRemoteResponse>
}