package com.london.data.repository.myrating

import com.london.data.local.preference.AuthPreferences
import com.london.data.remote.source.myrating.RatingRemoteDataSource
import com.london.domain.repository.RatingRepository
import javax.inject.Inject

class RatingRepositoryImpl @Inject constructor(
    private val ratingRemoteDataSource: RatingRemoteDataSource,
    private val authPreferences: AuthPreferences
) : RatingRepository {

    override suspend fun addMovieRatingById(
        id: Int,
        rating: Int,
    ): Boolean = ratingRemoteDataSource.addMovieRating(
        movieId = id,
        rating = rating.toDouble(),
        userSessionId = authPreferences.getSessionId(),
        guestSessionId = authPreferences.getGuestSessionId()
    ).isSuccess

    override suspend fun addTvShowById(id: Int, rating: Int): Boolean =
        ratingRemoteDataSource.addTvShowRating(
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
    ): Boolean = ratingRemoteDataSource.addTvEpisode(
        tvShowId = tvShowId,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber,
        userSessionId = authPreferences.getSessionId(),
        guestSessionId = authPreferences.getGuestSessionId(),
        rating = rating.toDouble()
    ).isSuccess


}
