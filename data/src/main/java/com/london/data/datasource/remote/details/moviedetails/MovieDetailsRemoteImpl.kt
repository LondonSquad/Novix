package com.london.data.datasource.remote.details.moviedetails

import com.london.data.datasource.remote.ApiConstants
import com.london.data.datasource.remote.details.moviedetails.model.moviecast.MovieCastResponse
import com.london.data.datasource.remote.details.moviedetails.model.moviedetails.MovieDetailsResponse
import com.london.data.datasource.remote.details.moviedetails.model.movieimages.MovieImagesResponse
import com.london.data.datasource.remote.details.moviedetails.model.similarmovies.SimilarMoviesResponse
import com.london.data.utils.get
import io.ktor.client.HttpClient
import org.koin.core.annotation.Single

@Single
class MovieDetailsRemoteImpl(
    private val ktorClient: HttpClient,
) : MovieDetailsRemote {
    override suspend fun getMovieDetails(movieId: Int): MovieDetailsResponse =
        ktorClient.get(ApiConstants.getMovieDetailsPath(movieId))

    override suspend fun getSimilarMovies(movieId: Int): SimilarMoviesResponse =
        ktorClient.get(ApiConstants.getSimilarMoviesPath(movieId))

    override suspend fun getMovieCast(movieId: Int): MovieCastResponse =
        ktorClient.get(ApiConstants.getMovieCastPath(movieId))

    override suspend fun getMovieImages(movieId: Int): MovieImagesResponse =
        ktorClient.get(ApiConstants.getMovieImagesPath(movieId))
}
