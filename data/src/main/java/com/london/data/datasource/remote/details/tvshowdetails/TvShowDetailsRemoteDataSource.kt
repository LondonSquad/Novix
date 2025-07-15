package com.london.data.datasource.remote.details.tvshowdetails

import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowCastRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowDetailsRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowEpisodesRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowImagesRemoteResponse

interface TvShowDetailsRemoteDataSource {
    suspend fun getTvShowDetailsById(
        tvShowId: Int,
    ): TvShowDetailsRemoteResponse

    suspend fun getTvShowEpisodesBySeason(
        tvShowId: Int,
        seasonNumber: Int,
    ): TvShowEpisodesRemoteResponse

    suspend fun getCastsByTvShowId(tvShowId: Int): TvShowCastRemoteResponse

    suspend fun getTvShowImagesById(tvShowId: Int): TvShowImagesRemoteResponse
}