package com.london.data.remote.source.myrating

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.rating.RatingRemoteResponse
import com.london.data.remote.model.myrating.RatingMediaResponse

interface RatingRemoteDataSource {
    suspend fun getAllRatedMovies(
        accountId: Int,
        sessionId: String,
    ): Result<ApiResponse<RatingMediaResponse>>

    suspend fun getAllRatedTvShows(
        accountId: Int,
        sessionId: String,
    ): Result<ApiResponse<RatingMediaResponse>>

    suspend fun addMovieRating(
        movieId: Int,
        rating: Double,
        userSessionId: String?,
        guestSessionId: String?
    ): Result<RatingRemoteResponse>

    suspend fun addTvShowRating(
        tvShowId: Int,
        rating: Double,
        userSessionId: String?,
        guestSessionId: String?
    ): Result<RatingRemoteResponse>

    suspend fun addTvEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Double,
        userSessionId: String?,
        guestSessionId: String?
    ): Result<RatingRemoteResponse>
}