package com.london.data.remote.source.details.tvshow

import com.london.data.remote.model.details.rating.AccountMediaStatesResponse
import com.london.data.remote.model.details.tvshow.model.TvShowCastRemoteResponse
import com.london.data.remote.model.details.tvshow.model.TvShowDetailsRemoteResponse
import com.london.data.remote.model.details.tvshow.model.TvShowImagesRemoteResponse
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.EpisodeVideoResponse
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.TvShowEpisodeResponse
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.TvShowEpisodesRemoteResponse

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

    suspend fun getEpisodeVideos(
        seriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Result<EpisodeVideoResponse>

    suspend fun getAccountTvShowStates(
        tvShowShow: Int,
        guestSessionId: String?,
        userSessionId: String?
    ): Result<AccountMediaStatesResponse>
}