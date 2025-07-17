package com.london.data.datasource.remote.details.moviedetails.model

import com.london.data.datasource.remote.ApiConstants
import com.london.data.datasource.remote.details.moviedetails.MovieDetailsRemote
import com.london.data.datasource.remote.details.moviedetails.fetchData
import com.london.data.datasource.remote.details.moviedetails.model.moviecast.MovieCastResponse
import com.london.data.datasource.remote.details.moviedetails.model.moviedetails.MovieDetailsResponse
import com.london.data.datasource.remote.details.moviedetails.model.movieimages.MovieImagesResponse
import com.london.data.datasource.remote.details.moviedetails.model.similarmovies.SimilarMoviesResponse
import io.ktor.client.HttpClient
import org.koin.core.annotation.Single

@Single
class MovieDetailsRemoteImpl(
    private val ktorClient: HttpClient,
) : MovieDetailsRemote {

    override suspend fun getMovieDetails(movieId: Int): MovieDetailsResponse = fetchData(
        path = "${ApiConstants.MOVIE_DETAILS_PATH}/$movieId",
        tag = "getMovieDetails",
        ktorClient = ktorClient
    )

    override suspend fun getSimilarMovies(movieId: Int): SimilarMoviesResponse = fetchData(
        path = "${ApiConstants.MOVIE_DETAILS_PATH}/$movieId/similar",
        tag = "getSimilarMovieRemotes",
        ktorClient = ktorClient
    )

    override suspend fun getMovieCast(movieId: Int): MovieCastResponse = fetchData(
        path = "${ApiConstants.MOVIE_DETAILS_PATH}/$movieId/credits",
        tag = "getActorDetails",
        ktorClient = ktorClient
    )

    override suspend fun getMovieImages(movieId: Int): MovieImagesResponse = fetchData(
        path = "${ApiConstants.MOVIE_DETAILS_PATH}/$movieId/images",
        tag = "getMovieImages",
        ktorClient = ktorClient
    )
}
