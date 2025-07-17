package com.london.data.datasource.remote.details.tvshowdetails

import com.london.data.datasource.remote.ApiConstants
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowCastRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowDetailsRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowImagesRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.TvShowEpisodeResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.TvShowEpisodesRemoteResponse
import com.london.data.utils.get
import io.ktor.client.HttpClient

class TvShowDetailsRemoteDataSourceImpl(
    private val ktorClient: HttpClient,
) : TvShowDetailsRemoteDataSource {

    override suspend fun getTvShowDetailsById(tvShowId: Int): TvShowDetailsRemoteResponse =
        ktorClient.get(ApiConstants.getTvShowDetailsPath(tvShowId))

    override suspend fun getTvShowEpisodesBySeason(
        tvShowId: Int,
        seasonNumber: Int
    ): TvShowEpisodesRemoteResponse =
        ktorClient.get(ApiConstants.getTvShowEpisodeBySeasonPath(tvShowId, seasonNumber))

    override suspend fun getCastsByTvShowId(tvShowId: Int): TvShowCastRemoteResponse =
        ktorClient.get(ApiConstants.getCastTvShowPath(tvShowId))

    override suspend fun getTvShowImagesById(tvShowId: Int): TvShowImagesRemoteResponse =
        ktorClient.get(ApiConstants.getImagesTvShowPath(tvShowId))

    override suspend fun getTvShowImagesById(tvShowId: Int): TvShowImagesRemoteResponse {
        val response = ktorClient.get {
            url {
                path(ApiConstants.getImagesTvShowPath(tvShowId))
                parameters.append("api_key", BuildConfig.API_KEY)
            }
        }
        return response.body()
    }

    override suspend fun getEpisodeDetailsByPosition(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): TvShowEpisodeResponse {
        val response = ktorClient.get {
            url {
                path(
                    ApiConstants.getTvShowEpisodeByEpisodePath(
                        tvShowId,
                        seasonNumber,
                        episodeNumber
                    )
                )
                parameters.append("api_key", BuildConfig.API_KEY)
            }
        }
        return response.body()
    }
}
