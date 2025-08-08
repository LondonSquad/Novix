package com.london.data.remote.source.details.tvshow

import com.london.data.remote.model.details.rating.AccountStatesResponse
import com.london.data.remote.model.details.tvshow.model.TvShowCastRemoteResponse
import com.london.data.remote.model.details.tvshow.model.TvShowDetailsRemoteResponse
import com.london.data.remote.model.details.tvshow.model.TvShowImagesRemoteResponse
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.EpisodeVideoResponse
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.TvShowEpisodeResponse
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.TvShowEpisodesRemoteResponse
import com.london.data.remote.service.details.tvshow.TvShowDetailsApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import javax.inject.Inject

class TvShowDetailsRemoteDataSourceImpl @Inject constructor(
    private val tvShowDetailsApiService: TvShowDetailsApiService,
) : TvShowDetailsRemoteDataSource, BaseRemoteDatasource {

    override suspend fun getTvShowDetailsById(id: Int): Result<TvShowDetailsRemoteResponse> =
        callApiWithRetry(
            apiCall = { tvShowDetailsApiService.getTvShowDetails(tvShowId = id) },
            mapper = { it }
        )

    override suspend fun getTvShowEpisodesBySeason(
        id: Int,
        seasonNumber: Int
    ): Result<TvShowEpisodesRemoteResponse> =
        callApiWithRetry(
            apiCall = {
                tvShowDetailsApiService.getTvShowEpisodesBySeason(
                    tvShowId = id,
                    seasonNumber = seasonNumber
                )
            },
            mapper = { it }
        )

    override suspend fun getCastsByTvShowId(id: Int): Result<TvShowCastRemoteResponse> =
        callApiWithRetry(
            apiCall = { tvShowDetailsApiService.getTvShowCast(tvShowId = id) },
            mapper = { it }
        )

    override suspend fun getTvShowImagesById(id: Int): Result<TvShowImagesRemoteResponse> =
        callApiWithRetry(
            apiCall = { tvShowDetailsApiService.getTvShowImages(tvShowId = id) },
            mapper = { it }
        )

    override suspend fun getEpisodeDetailsByPosition(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Result<TvShowEpisodeResponse> =
        callApiWithRetry(
            apiCall = {
                tvShowDetailsApiService.getEpisodeDetails(
                    tvShowId = tvShowId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber
                )
            },
            mapper = { it }
        )

    override suspend fun getEpisodeVideos(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): Result<EpisodeVideoResponse> =
        callApiWithRetry(
            apiCall = {
                tvShowDetailsApiService.getEpisodeVideo(
                    seriesId = tvShowId,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber
                )
            },
            mapper = { it }
        )

    override suspend fun getAccountTvShowStates(
        tvShowId: Int,
        guestSessionId: String?,
        userSessionId: String?,
    ): Result<AccountStatesResponse> = callApiWithRetry(
        apiCall = {
            tvShowDetailsApiService.getAccountTvShowState(
                seriesId = tvShowId,
                guestSessionId = guestSessionId,
                userSessionId = userSessionId
            )
        },
        mapper = { it }
    )

    override suspend fun getAccountTvEpisodeState(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        guestSessionId: String?,
        userSessionId: String?
    ): Result<AccountStatesResponse> = callApiWithRetry(
        apiCall = {
            tvShowDetailsApiService.getAccountTvEpisode(
                tvShowId = tvShowId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber,
                guestSessionId = guestSessionId,
                userSessionId = userSessionId
            )
        },
        mapper = { it }
    )
}
