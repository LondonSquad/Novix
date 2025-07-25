package com.london.data.datasource.remote.details.videoprovider.tvshow

import com.london.data.datasource.remote.details.videoprovider.tvshow.model.TvShowVideoResponse

interface TvShowVideoProviderRemote {
    suspend fun getTvShowVideos(tvShowId: Int): Result<TvShowVideoResponse>
}