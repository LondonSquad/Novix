package com.london.data.remote.source.details.movie

import com.london.data.remote.model.details.RatingRemote
import com.london.data.remote.model.details.RatingResponse
import com.london.data.remote.service.details.movie.AddMovieRatingApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import javax.inject.Inject

class AddMovieRatingRemoteDataSourceImpl @Inject constructor(
    private val addMovieRatingApiService: AddMovieRatingApiService
) : AddMovieRatingRemoteDataSource, BaseRemoteDatasource {
    override suspend fun addMovieRating(
        movieId: Int,
        rating: Double,
        guestSessionId: String?,
        userSessionId: String?
    ): Result<RatingResponse> = callApiWithRetry(apiCall = {
        addMovieRatingApiService.addMovieRating(
            movieId = movieId,
            ratingRequest = RatingRemote(rating),
            guestSessionId = guestSessionId,
            userSessionId = userSessionId
        )
    }, mapper = { it })
}