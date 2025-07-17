package com.london.data.datasource.remote.details.tvshowdetails

import com.london.data.BuildConfig
import com.london.data.datasource.remote.ApiConstants
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowCastRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowDetailsRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowImagesRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.TvShowEpisodesRemoteResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
class TvShowDetailsRemoteDataSourceImpl(
    private val ktorClient: HttpClient,
) : TvShowDetailsRemoteDataSource {
    override suspend fun getTvShowDetailsById(
        tvShowId: Int,
    ): TvShowDetailsRemoteResponse {
        val response = ktorClient.get {
            url {
                path(ApiConstants.getTvShowDetailsPath(tvShowId))
                parameters.append("api_key", BuildConfig.API_KEY)
            }
        }
        return response.body()
    }

    override suspend fun getTvShowEpisodesBySeason(
        tvShowId: Int,
        seasonNumber: Int,
    ): TvShowEpisodesRemoteResponse {
        val response = ktorClient.get {
            url {
                path(ApiConstants.getTvShowEpisodeBySeasonPath(tvShowId, seasonNumber))
                parameters.append("api_key", BuildConfig.API_KEY)
            }
        }
        return response.body()
    }

    override suspend fun getCastsByTvShowId(tvShowId: Int): TvShowCastRemoteResponse {
        val response = ktorClient.get {
            url {
                path(ApiConstants.getCastTvShowPath(tvShowId))
                parameters.append("api_key", BuildConfig.API_KEY)
            }
        }
        return response.body()
    }

    override suspend fun getTvShowImagesById(tvShowId: Int): TvShowImagesRemoteResponse {
        val response = ktorClient.get {
            url {
                path(ApiConstants.getImagesTvShowPath(tvShowId))
                parameters.append("api_key", BuildConfig.API_KEY)
            }
        }
        return response.body()
    }
}
