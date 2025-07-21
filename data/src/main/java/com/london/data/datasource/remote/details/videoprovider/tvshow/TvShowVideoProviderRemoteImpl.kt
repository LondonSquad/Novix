package com.london.data.datasource.remote.details.videoprovider.tvshow

import com.london.data.datasource.remote.details.tvshowdetails.api.TvShowDetailsApiService
import com.london.data.datasource.remote.details.videoprovider.tvshow.TvShowVideoProviderRemote
import com.london.data.datasource.remote.details.videoprovider.tvshow.model.TvShowVideoResponse
import org.koin.core.annotation.Single

@Single
class TvShowVideoProviderRemoteImpl(
    private val tvShowDetailsApiService: TvShowDetailsApiService,
) : TvShowVideoProviderRemote {
    override suspend fun getTvShowVideos(tvShowId: Int): TvShowVideoResponse =
        tvShowDetailsApiService.getTvShowVideos(
            tvShowId = tvShowId
        )
}