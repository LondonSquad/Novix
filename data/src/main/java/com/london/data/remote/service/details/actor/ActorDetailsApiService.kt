package com.london.data.remote.service.details.actor

import com.london.data.remote.model.ApiConstants
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.actor.model.ActorDetailsResponse
import com.london.data.remote.model.details.actor.model.actorimage.ActorImageResponse
import com.london.data.remote.model.details.movie.model.moviecast.MovieCastResponse
import com.london.data.remote.model.details.tvshow.model.TvShowCastRemoteResponse
import com.london.data.remote.model.home.trending.TrendingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ActorDetailsApiService {

    @GET("3/person/{person_id}")
    suspend fun getActorDetails(
        @Path("person_id") actorId: Int,
    ): Response<ActorDetailsResponse>

    @GET("3/person/{person_id}/images")
    suspend fun getActorImages(
        @Path("person_id") actorId: Int,
    ): Response<ActorImageResponse>

    @GET(ApiConstants.TRENDING_ACTORS_PATH)
    suspend fun getTrendingActors(@Query("page") page: Int): Response<ApiResponse<TrendingResponse>>

    @GET("3/tv/{tv_id}/aggregate_credits")
    suspend fun getTvShowCast(
        @Path("tv_id") tvShowId: Int,
    ): Response<TvShowCastRemoteResponse>

    @GET("3/movie/{movie_id}/credits")
    suspend fun getMovieCast(
        @Path("movie_id") movieId: Int,
    ): Response<MovieCastResponse>
}