package com.london.data.remote.source.details.tvshow

import com.london.data.datasource.remote.BaseRemoteDatasource
import com.london.data.datasource.remote.details.tvshowdetails.api.TvShowDetailsApiService
import com.london.data.remote.model.details.tvshow.model.TvShowCastRemoteResponse
import com.london.data.remote.model.details.tvshow.model.TvShowDetailsRemoteResponse
import com.london.data.remote.model.details.tvshow.model.TvShowImagesRemoteResponse
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.TvShowEpisodeResponse
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.TvShowEpisodesRemoteResponse
import org.koin.core.annotation.Single

@Single
class TvShowDetailsRemoteDataSourceImpl(
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
}
