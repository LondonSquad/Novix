package com.london.data.repository.rating

import com.london.data.local.preference.AuthPreferences
import com.london.data.remote.source.details.movie.rating.AddMovieRatingRemoteDataSource
import com.london.domain.repository.RatingRepository
import javax.inject.Inject

class RatingRepositoryImpl @Inject constructor(
    private val addMovieRatingRemoteDataSource: AddMovieRatingRemoteDataSource,
    private val authPreferences: AuthPreferences
) : RatingRepository {
    override suspend fun addMovieRatingById(
        id: Int,
        rating: Int,
    ): Boolean = addMovieRatingRemoteDataSource.addMovieRating(
        movieId = id,
        rating = rating.toDouble(),
        userSessionId = authPreferences.getSessionId(),
        guestSessionId = authPreferences.getGuestSessionId()
    ).isSuccess
}
