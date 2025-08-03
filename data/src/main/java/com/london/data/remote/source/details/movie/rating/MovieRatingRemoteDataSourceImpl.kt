package com.london.data.remote.source.details.movie.rating

import com.london.data.remote.model.details.rating.RatingRemoteBody
import com.london.data.remote.model.details.rating.RatingRemoteResponse
import com.london.data.remote.service.details.movie.MovieRatingApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import javax.inject.Inject

class MovieRatingRemoteDataSourceImpl @Inject constructor(
    private val movieRatingApiService: MovieRatingApiService
) : MovieRatingRemoteDataSource, BaseRemoteDatasource {
    override suspend fun addMovieRating(
        movieId: Int,
        rating: Double,
        guestSessionId: String?,
        userSessionId: String?
    ): Result<RatingRemoteResponse> = callApiWithRetry(apiCall = {
        movieRatingApiService.addMovieRating(
            movieId = movieId,
            ratingRequest = RatingRemoteBody(rating),
            guestSessionId = guestSessionId,
            userSessionId = userSessionId
        )
    }, mapper = { it })
}