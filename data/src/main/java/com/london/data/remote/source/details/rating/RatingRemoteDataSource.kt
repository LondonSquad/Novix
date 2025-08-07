package com.london.data.remote.source.details.rating

import com.london.data.remote.model.details.rating.RatingRemoteResponse

interface RatingRemoteDataSource {
    suspend fun addMovieRating(
        movieId: Int,
        rating: Double,
        guestSessionId: String?,
        userSessionId: String?
    ): Result<RatingRemoteResponse>

    suspend fun addTvShowRating(
        tvShowId: Int,
        guestSessionId: String?,
        userSessionId: String?,
        rating: Double
    ): Result<RatingRemoteResponse>

    suspend fun addTvEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        guestSessionId: String?,
        userSessionId: String?,
        rating: Double
    ): Result<RatingRemoteResponse>
}