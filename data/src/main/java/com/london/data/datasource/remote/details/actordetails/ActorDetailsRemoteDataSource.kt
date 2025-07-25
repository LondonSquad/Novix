package com.london.data.datasource.remote.details.actordetails

import com.london.data.datasource.remote.details.actordetails.model.ActorDetailsResponse
import com.london.data.datasource.remote.details.actordetails.model.actorimage.ActorImageResponse
import com.london.data.datasource.remote.details.actordetails.model.actormoviedetails.ActorMovieDetailsResponse
import com.london.data.datasource.remote.details.actordetails.model.actortvshowdetails.ActorTvShowDetailsResponse

interface ActorDetailsRemoteDataSource {
    suspend fun getActorDetailsById(id: Int): Result<ActorDetailsResponse>
    suspend fun getActorMovieById(id: Int): Result<ActorMovieDetailsResponse>
    suspend fun getActorTvShowById(id: Int): Result<ActorTvShowDetailsResponse>
    suspend fun getActorImagePath(id: Int): Result<ActorImageResponse>
}