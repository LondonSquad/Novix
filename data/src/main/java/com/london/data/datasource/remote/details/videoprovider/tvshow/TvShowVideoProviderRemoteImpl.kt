package com.london.data.datasource.remote.details.videoprovider.tvshow

import com.london.data.datasource.remote.BaseRemoteDatasource
import com.london.data.datasource.remote.details.tvshowdetails.api.TvShowDetailsApiService
import com.london.data.datasource.remote.details.videoprovider.tvshow.model.TvShowVideoResponse
import org.koin.core.annotation.Single

@Single
class TvShowVideoProviderRemoteImpl(
    private val tvShowDetailsApiService: TvShowDetailsApiService,
) : TvShowVideoProviderRemote, BaseRemoteDatasource {
    override suspend fun getTvShowVideos(tvShowId: Int): Result<TvShowVideoResponse> =
        callApiWithRetry(
            apiCall = { tvShowDetailsApiService.getTvShowVideos(tvShowId = tvShowId) },
            mapper = { it }
        )
}
