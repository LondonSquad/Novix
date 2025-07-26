package com.london.data.remote.source.details.videoprovider.tvshow

import com.london.data.remote.model.details.videoprovider.tvshow.model.TvShowVideoResponse
import com.london.data.remote.service.details.tvshow.TvShowDetailsApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
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
