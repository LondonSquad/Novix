package com.london.data.datasource.remote.details.moviedetails

import com.london.data.datasource.remote.ApiConstants
import com.london.data.datasource.remote.details.moviedetails.model.moviecast.MovieCastResponse
import com.london.data.datasource.remote.details.moviedetails.model.moviedetails.MovieDetailsResponse
import com.london.data.datasource.remote.details.moviedetails.model.movieimages.MovieImagesResponse
import com.london.data.datasource.remote.details.moviedetails.model.similarmovies.SimilarMoviesResponse
import io.ktor.client.HttpClient

class MovieDetailsRemoteImpl(
    private val ktorClient: HttpClient,
) : MovieDetailsRemote {

    override suspend fun getMovieDetails(movieId: Int): MovieDetailsResponse = fetchData(
        path = "${ApiConstants.MOVIE_DETAILS_PATH}/$movieId",
        ktorClient = ktorClient
    )

    override suspend fun getSimilarMovies(movieId: Int): SimilarMoviesResponse = fetchData(
        path = "${ApiConstants.MOVIE_DETAILS_PATH}/$movieId/similar",
        ktorClient = ktorClient
    )

    override suspend fun getMovieCast(movieId: Int): MovieCastResponse = fetchData(
        path = "${ApiConstants.MOVIE_DETAILS_PATH}/$movieId/credits",
        ktorClient = ktorClient
    )

    override suspend fun getMovieImages(movieId: Int): MovieImagesResponse = fetchData(
        path = "${ApiConstants.MOVIE_DETAILS_PATH}/$movieId/images",
        ktorClient = ktorClient
    )
}
