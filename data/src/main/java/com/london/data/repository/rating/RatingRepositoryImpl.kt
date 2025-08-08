package com.london.data.repository.rating

import com.london.data.local.preference.AuthPreferences
import com.london.data.remote.source.details.rating.RatingRemoteDataSource
import com.london.domain.repository.RatingRepository
import javax.inject.Inject

class RatingRepositoryImpl @Inject constructor(
    private val movieRatingRemoteDataSource: RatingRemoteDataSource,
    private val authPreferences: AuthPreferences
) : RatingRepository {
    override suspend fun addMovieRatingById(
        id: Int,
        rating: Int,
    ): Boolean = movieRatingRemoteDataSource.addMovieRating(
        movieId = id,
        rating = rating.toDouble(),
        userSessionId = authPreferences.getSessionId(),
        guestSessionId = authPreferences.getGuestSessionId()
    ).isSuccess

    override suspend fun addTvShowById(id: Int, rating: Int): Boolean =
        movieRatingRemoteDataSource.addTvShowRating(
            tvShowId = id,
            rating = rating.toDouble(),
            userSessionId = authPreferences.getSessionId(),
            guestSessionId = authPreferences.getGuestSessionId()
        ).isSuccess

    override suspend fun addTvEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Int
    ): Boolean = movieRatingRemoteDataSource.addTvEpisode(
        tvShowId = tvShowId,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber,
        guestSessionId = authPreferences.getSessionId(),
        userSessionId = authPreferences.getSessionId(),
        rating = rating.toDouble()
    ).isSuccess
}
