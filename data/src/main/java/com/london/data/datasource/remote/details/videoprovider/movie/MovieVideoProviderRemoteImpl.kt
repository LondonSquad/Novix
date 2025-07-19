package com.london.data.datasource.remote.details.videoprovider.movie

import com.london.data.datasource.device.DeviceConfigurationDataSource
import com.london.data.datasource.remote.ApiConstants
import com.london.data.datasource.remote.details.videoprovider.movie.model.MovieVideoProviderRemote
import com.london.data.datasource.remote.details.videoprovider.movie.model.MovieVideoResponse
import com.london.data.utils.get
import io.ktor.client.HttpClient
import org.koin.core.annotation.Single

@Single
class MovieVideoProviderRemoteImpl(
    private val ktorClient: HttpClient,
    private val deviceConfigurationDataSource: DeviceConfigurationDataSource
) : MovieVideoProviderRemote {
    override suspend fun getMovieVideos(movieId: Int): MovieVideoResponse =
        ktorClient.get(
            path = ApiConstants.getMovieVideosPath(movieId),
            params = mapOf("language" to deviceConfigurationDataSource.getCurrentLanguage())
        )
}
