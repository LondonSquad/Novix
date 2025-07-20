package com.london.data.datasource.remote.details.tvshowdetails

import com.london.data.datasource.remote.details.tvshowdetails.api.TvShowDetailsApiService
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowCastRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowDetailsRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowImagesRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.TvShowEpisodeResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.TvShowEpisodesRemoteResponse
import org.koin.core.annotation.Single

@Single
class TvShowDetailsRemoteDataSourceImpl(
    private val tvShowDetailsApiService: TvShowDetailsApiService,
) : TvShowDetailsRemoteDataSource {

    override suspend fun getTvShowDetailsById(tvShowId: Int): TvShowDetailsRemoteResponse =
        tvShowDetailsApiService.getTvShowDetails(
            tvShowId = tvShowId
        )

    override suspend fun getTvShowEpisodesBySeason(
        tvShowId: Int,
        seasonNumber: Int
    ): TvShowEpisodesRemoteResponse =
        tvShowDetailsApiService.getTvShowEpisodesBySeason(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber
        )

    override suspend fun getCastsByTvShowId(tvShowId: Int): TvShowCastRemoteResponse =
        tvShowDetailsApiService.getTvShowCast(
            tvShowId = tvShowId
        )

    override suspend fun getTvShowImagesById(tvShowId: Int): TvShowImagesRemoteResponse =
        tvShowDetailsApiService.getTvShowImages(
            tvShowId = tvShowId
        )

    override suspend fun getEpisodeDetailsByPosition(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): TvShowEpisodeResponse =
        tvShowDetailsApiService.getEpisodeDetails(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber
        )
}
