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

    override suspend fun getTvShowDetailsById(id: Int): TvShowDetailsRemoteResponse =
        tvShowDetailsApiService.getTvShowDetails(
            tvShowId = id
        )

    override suspend fun getTvShowEpisodesBySeason(
        tvShowId: Int,
        seasonNumber: Int
    ): TvShowEpisodesRemoteResponse =
        tvShowDetailsApiService.getTvShowEpisodesBySeason(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber
        )

    override suspend fun getCastsByTvShowId(id: Int): TvShowCastRemoteResponse =
        tvShowDetailsApiService.getTvShowCast(
            tvShowId = id
        )

    override suspend fun getTvShowImagesById(id: Int): TvShowImagesRemoteResponse =
        tvShowDetailsApiService.getTvShowImages(
            tvShowId = id
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
