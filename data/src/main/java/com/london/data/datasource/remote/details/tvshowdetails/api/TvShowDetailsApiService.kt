package com.london.data.datasource.remote.details.tvshowdetails.api

import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowCastRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowDetailsRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowImagesRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.TvShowEpisodeResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode.TvShowEpisodesRemoteResponse
import com.london.data.datasource.remote.details.videoprovider.tvshow.model.TvShowVideoResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface TvShowDetailsApiService {

    @GET("3/tv/{tv_id}")
    suspend fun getTvShowDetails(
        @Path("tv_id") tvShowId: Int,
    ): TvShowDetailsRemoteResponse

    @GET("3/tv/{tv_id}/season/{season_number}")
    suspend fun getTvShowEpisodesBySeason(
        @Path("tv_id") tvShowId: Int,
        @Path("season_number") seasonNumber: Int,
    ): TvShowEpisodesRemoteResponse

    @GET("3/tv/{tv_id}/aggregate_credits")
    suspend fun getTvShowCast(
        @Path("tv_id") tvShowId: Int,
    ): TvShowCastRemoteResponse

    @GET("3/tv/{tv_id}/images")
    suspend fun getTvShowImages(
        @Path("tv_id") tvShowId: Int,
    ): TvShowImagesRemoteResponse

    @GET("3/tv/{tv_id}/season/{season_number}/episode/{episode_number}")
    suspend fun getEpisodeDetails(
        @Path("tv_id") tvShowId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
    ): TvShowEpisodeResponse

    @GET("3/tv/{tv_id}/videos")
    suspend fun getTvShowVideos(
        @Path("tv_id") tvShowId: Int,
    ): TvShowVideoResponse
}