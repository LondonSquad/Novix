package com.london.data.remote.service.actor

import com.london.data.remote.model.ApiConstants
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.actor.ActorDetailsResponse
import com.london.data.remote.model.details.actor.image.ActorImageResponse
import com.london.data.remote.model.details.movie.cast.MovieCastResponse
import com.london.data.remote.model.details.tvshow.TvShowCastRemoteResponse
import com.london.data.remote.model.trending.TrendingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ActorApiService {

    @GET(ApiConstants.GET_ACTOR_DETAILS_PATH)
    suspend fun getActorDetails(
        @Path("person_id") actorId: Int,
    ): Response<ActorDetailsResponse>

    @GET(ApiConstants.GET_ACTOR_IMAGES_PATH)
    suspend fun getActorImages(
        @Path("person_id") actorId: Int,
    ): Response<ActorImageResponse>

    @GET(ApiConstants.TRENDING_ACTORS_PATH)
    suspend fun getTrendingActors(@Query("page") page: Int): Response<ApiResponse<TrendingResponse>>

    @GET(ApiConstants.GET_TV_SHOW_CAST)
    suspend fun getTvShowActors(
        @Path("tv_id") tvShowId: Int,
    ): Response<TvShowCastRemoteResponse>

    @GET(ApiConstants.GET_MOVIE_CAST)
    suspend fun getMovieActors(
        @Path("movie_id") movieId: Int,
    ): Response<MovieCastResponse>
}
