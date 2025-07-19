package com.london.data.datasource.remote.details.tvshowdetails

import com.london.data.datasource.device.DeviceConfigurationDataSource
import com.london.data.datasource.remote.ApiConstants
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowCastRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowDetailsRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowImagesRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.TvShowEpisodeResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.TvShowEpisodesRemoteResponse
import com.london.data.utils.get
import io.ktor.client.HttpClient
import org.koin.core.annotation.Single

@Single
class TvShowDetailsRemoteDataSourceImpl(
    private val ktorClient: HttpClient,
    private val deviceConfigurationDataSource: DeviceConfigurationDataSource
) : TvShowDetailsRemoteDataSource {

    override suspend fun getTvShowDetailsById(tvShowId: Int): TvShowDetailsRemoteResponse =
        ktorClient.get(
            path = ApiConstants.getTvShowDetailsPath(tvShowId),
            params = mapOf("language" to deviceConfigurationDataSource.getCurrentLanguage())
        )

    override suspend fun getTvShowEpisodesBySeason(
        tvShowId: Int,
        seasonNumber: Int
    ): TvShowEpisodesRemoteResponse =
        ktorClient.get(
            path = ApiConstants.getTvShowEpisodeBySeasonPath(tvShowId, seasonNumber),
            params = mapOf("language" to deviceConfigurationDataSource.getCurrentLanguage())
        )

    override suspend fun getCastsByTvShowId(tvShowId: Int): TvShowCastRemoteResponse =
        ktorClient.get(
            path = ApiConstants.getCastTvShowPath(tvShowId),
            params = mapOf("language" to deviceConfigurationDataSource.getCurrentLanguage())
        )

    override suspend fun getTvShowImagesById(tvShowId: Int): TvShowImagesRemoteResponse =
        ktorClient.get(
            path = ApiConstants.getImagesTvShowPath(tvShowId),
            params = mapOf("language" to deviceConfigurationDataSource.getCurrentLanguage())
        )

    override suspend fun getEpisodeDetailsByPosition(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): TvShowEpisodeResponse = ktorClient.get(
        path = ApiConstants.getTvShowEpisodeByEpisodePath(
            tvShowId,
            seasonNumber,
            episodeNumber
        ),
        params = mapOf("language" to deviceConfigurationDataSource.getCurrentLanguage())
    )
}
