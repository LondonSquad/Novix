package com.london.data.datasource.remote.details.videoprovider.tvshow

import com.london.data.datasource.device.DeviceConfigurationDataSource
import com.london.data.datasource.remote.ApiConstants
import com.london.data.utils.get
import io.ktor.client.HttpClient
import org.koin.core.annotation.Single

@Single
class TvShowVideoProviderRemoteImpl(
    private val ktorClient: HttpClient,
    private val deviceConfigurationDataSource: DeviceConfigurationDataSource
) : TvShowVideoProviderRemote {
    override suspend fun getTvShowVideos(tvShowId: Int): TvShowVideoResponse =
        ktorClient.get(
           path =  ApiConstants.getTvShowVideosPath(tvShowId),
            params = mapOf("language" to deviceConfigurationDataSource.getCurrentLanguage())
        )
}