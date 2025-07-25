package com.london.data.datasource.remote.details.tvshowdetails

import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowCastRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowDetailsRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowImagesRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.TvShowEpisodeResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.TvShowEpisodesRemoteResponse

interface TvShowDetailsRemoteDataSource {
    suspend fun getTvShowDetailsById(
        id: Int,
    ): Result<TvShowDetailsRemoteResponse>

    suspend fun getTvShowEpisodesBySeason(
        id: Int,
        seasonNumber: Int,
    ): Result<TvShowEpisodesRemoteResponse>

    suspend fun getCastsByTvShowId(id: Int): Result<TvShowCastRemoteResponse>

    suspend fun getTvShowImagesById(id: Int): Result<TvShowImagesRemoteResponse>

    suspend fun getEpisodeDetailsByPosition(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Result<TvShowEpisodeResponse>
}