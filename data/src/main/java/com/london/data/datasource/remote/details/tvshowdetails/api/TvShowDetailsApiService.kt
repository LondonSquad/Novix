package com.london.data.datasource.remote.details.tvshowdetails.api

import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowCastRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowDetailsRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowImagesRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.TvShowEpisodeResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.TvShowEpisodesRemoteResponse
import com.london.data.datasource.remote.details.videoprovider.tvshow.model.TvShowVideoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TvShowDetailsApiService {

    @GET("3/tv/{tv_id}")
    suspend fun getTvShowDetails(
        @Path("tv_id") tvShowId: Int,
    ): Response<TvShowDetailsRemoteResponse>

    @GET("3/tv/{tv_id}/season/{season_number}")
    suspend fun getTvShowEpisodesBySeason(
        @Path("tv_id") tvShowId: Int,
        @Path("season_number") seasonNumber: Int,
    ): Response<TvShowEpisodesRemoteResponse>

    @GET("3/tv/{tv_id}/aggregate_credits")
    suspend fun getTvShowCast(
        @Path("tv_id") tvShowId: Int,
    ): Response<TvShowCastRemoteResponse>

    @GET("3/tv/{tv_id}/images")
    suspend fun getTvShowImages(
        @Path("tv_id") tvShowId: Int,
    ): Response<TvShowImagesRemoteResponse>

    @GET("3/tv/{tv_id}/season/{season_number}/episode/{episode_number}")
    suspend fun getEpisodeDetails(
        @Path("tv_id") tvShowId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
    ): Response<TvShowEpisodeResponse>

    @GET("3/tv/{tv_id}/videos")
    suspend fun getTvShowVideos(
        @Path("tv_id") tvShowId: Int,
    ): Response<TvShowVideoResponse>
}