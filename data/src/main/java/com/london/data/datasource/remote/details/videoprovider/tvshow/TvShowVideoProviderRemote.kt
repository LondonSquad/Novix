package com.london.data.datasource.remote.details.videoprovider.tvshow

interface TvShowVideoProviderRemote {
    suspend fun getTvShowVideos(tvShowId: Int): TvShowVideoResponse
}