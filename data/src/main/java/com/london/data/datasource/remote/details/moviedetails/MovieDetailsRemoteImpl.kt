package com.london.data.datasource.remote.details.moviedetails

import com.london.data.datasource.device.DeviceConfigurationDataSource
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
    private val deviceConfigurationDataSource: DeviceConfigurationDataSource
) : MovieDetailsRemote {
    override suspend fun getMovieDetails(movieId: Int): MovieDetailsResponse =
        ktorClient.get(
            ApiConstants.getMovieDetailsPath(movieId),
            params = mapOf("language" to deviceConfigurationDataSource.getCurrentLanguage())
        )

    override suspend fun getSimilarMovies(movieId: Int): SimilarMoviesResponse =
        ktorClient.get(
            ApiConstants.getSimilarMoviesPath(movieId),
            params = mapOf("language" to deviceConfigurationDataSource.getCurrentLanguage())
        )

    override suspend fun getMovieCast(movieId: Int): MovieCastResponse =
        ktorClient.get(
            ApiConstants.getMovieCastPath(movieId),
            params = mapOf("language" to deviceConfigurationDataSource.getCurrentLanguage())
        )

    override suspend fun getMovieImages(movieId: Int): MovieImagesResponse =
        ktorClient.get(
            ApiConstants.getMovieImagesPath(movieId),
            params = mapOf("language" to deviceConfigurationDataSource.getCurrentLanguage())
        )
}
