package com.london.data.datasource.remote.details.tvshowdetails

import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowCastRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowDetailsRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowImagesRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.TvShowEpisodeResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.TvShowEpisodesRemoteResponse

interface TvShowDetailsRemoteDataSource {
    suspend fun getTvShowDetailsById(
        tvShowId: Int,
    ): Result<TvShowDetailsRemoteResponse>

    suspend fun getTvShowEpisodesBySeason(
        tvShowId: Int,
        seasonNumber: Int,
    ): Result<TvShowEpisodesRemoteResponse>

    suspend fun getCastsByTvShowId(tvShowId: Int): Result<TvShowCastRemoteResponse>

    suspend fun getTvShowImagesById(tvShowId: Int): Result<TvShowImagesRemoteResponse>

    suspend fun getEpisodeDetailsByPosition(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Result<TvShowEpisodeResponse>
}