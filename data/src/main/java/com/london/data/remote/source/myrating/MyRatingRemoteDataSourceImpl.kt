package com.london.data.remote.source.myrating

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.rating.RatingRemoteBody
import com.london.data.remote.model.details.rating.RatingRemoteResponse
import com.london.data.remote.model.myrating.RatedMediaResponse
import com.london.data.remote.service.myrating.MyRatingApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import javax.inject.Inject

class MyRatingRemoteDataSourceImpl @Inject constructor(
    private val myRatingApiResponse: MyRatingApiService
) : MyRatingRemoteDataSource, BaseRemoteDatasource {

    override suspend fun getAllRatedMovies(
        accountId: Int,
        sessionId: String,
    ): Result<ApiResponse<RatedMediaResponse>> {
        return callApiWithRetry(
            apiCall = {
                myRatingApiResponse.getRatedMovies(
                    accountId = accountId,
                    sessionId = sessionId,
                )
            },
            mapper = { it }
        )
    }

    override suspend fun getAllRatedTvShows(
        accountId: Int,
        sessionId: String
    ): Result<ApiResponse<RatedMediaResponse>> {
        return callApiWithRetry(
            apiCall = {
                myRatingApiResponse.getRatedTvShow(
                    accountId = accountId,
                    sessionId = sessionId,
                )
            },
            mapper = { it }
        )
    }

    override suspend fun addMovieRating(
        movieId: Int,
        rating: Double,
        userSessionId: String?,
        guestSessionId: String?
    ): Result<RatingRemoteResponse> {
        return callApiWithRetry(
            apiCall = {
                myRatingApiResponse.addMovieRating(
                    movieId = movieId,
                    guestSessionId = guestSessionId,
                    userSessionId = userSessionId,
                    ratingRequest = RatingRemoteBody(value = rating)
                )
            },
            mapper = { it }
        )
    }

    override suspend fun addTvShowRating(
        tvShowId: Int,
        rating: Double,
        userSessionId: String?,
        guestSessionId: String?
    ): Result<RatingRemoteResponse> {
        return callApiWithRetry(
            apiCall = {
                myRatingApiResponse.addTvShowRating(
                    tvShowId = tvShowId,
                    guestSessionId = guestSessionId,
                    userSessionId = userSessionId,
                    ratingRequest = RatingRemoteBody(value = rating)
                )
            },
            mapper = { it }
        )
    }

    override suspend fun addTvEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Double,
        userSessionId: String?,
        guestSessionId: String?
    ): Result<RatingRemoteResponse> {
        return callApiWithRetry(
            apiCall = {
                myRatingApiResponse.addTvEpisode(
                    tvShowId = tvShowId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber,
                    guestSessionId = guestSessionId,
                    userSessionId = userSessionId,
                    ratingRequest = RatingRemoteBody(value = rating)
                )
            },
            mapper = { it }
        )
    }
}