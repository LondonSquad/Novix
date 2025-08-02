package com.london.data.repository

import com.london.data.local.preference.AuthPreferences
import com.london.data.remote.source.details.movie.rating.AddMovieRatingRemoteDataSource
import com.london.domain.repository.AddMovieRatingRepository
import javax.inject.Inject

class AddMovieRatingRepositoryImpl @Inject constructor(
    private val addMovieRatingRemoteDataSource: AddMovieRatingRemoteDataSource,
    private val authPreferences: AuthPreferences
) : AddMovieRatingRepository {
    override suspend fun addMovieRatingById(
        movieId: Int,
        rating: Int,
    ): Boolean {
        val userSessionId = authPreferences.getSessionId()
        val guestSessionId = authPreferences.getGuestSessionId()

        val result = when {
            userSessionId.isNullOrBlank().not() -> addMovieRatingRemoteDataSource.addMovieRating(
                    movieId = movieId,
                    rating = rating.toDouble(),
                    userSessionId = userSessionId,
                    guestSessionId = null
                )

            guestSessionId.isNullOrBlank().not() -> addMovieRatingRemoteDataSource.addMovieRating(
                movieId = movieId,
                rating = rating.toDouble(),
                userSessionId = null,
                guestSessionId = guestSessionId
            )
            else -> return false
        }
        return result.isSuccess
    }
}
