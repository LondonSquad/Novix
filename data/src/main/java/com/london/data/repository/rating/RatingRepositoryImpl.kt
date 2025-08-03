package com.london.data.repository.rating

import com.london.data.local.preference.AuthPreferences
import com.london.data.remote.source.details.movie.rating.MovieRatingRemoteDataSource
import com.london.domain.repository.RatingRepository
import javax.inject.Inject

class RatingRepositoryImpl @Inject constructor(
    private val movieRatingRemoteDataSource: MovieRatingRemoteDataSource,
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
}
