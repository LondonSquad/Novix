package com.london.data.datasource.remote.details.actordetails.api

import com.london.data.datasource.remote.details.actordetails.model.ActorDetailsResponse
import com.london.data.datasource.remote.details.actordetails.model.actorimage.ActorImageResponse
import com.london.data.datasource.remote.details.actordetails.model.actormoviedetails.ActorMovieDetailsResponse
import com.london.data.datasource.remote.details.actordetails.model.actortvshowdetails.ActorTvShowDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ActorDetailsApiService {

    @GET("3/person/{person_id}")
    suspend fun getActorDetails(
        @Path("person_id") actorId: Int,
    ): ActorDetailsResponse

    @GET("3/person/{person_id}/movie_credits")
    suspend fun getActorMovies(
        @Path("person_id") actorId: Int,
    ): ActorMovieDetailsResponse

    @GET("3/person/{person_id}/tv_credits")
    suspend fun getActorTvShows(
        @Path("person_id") actorId: Int,
    ): ActorTvShowDetailsResponse

    @GET("3/person/{person_id}/images")
    suspend fun getActorImages(
        @Path("person_id") actorId: Int,
    ): ActorImageResponse
}