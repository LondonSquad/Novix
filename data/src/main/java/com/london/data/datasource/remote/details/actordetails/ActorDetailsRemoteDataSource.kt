package com.london.data.datasource.remote.details.actordetails

import com.london.data.datasource.remote.details.actordetails.model.ActorDetailsRemoteResponse
import com.london.data.datasource.remote.details.actordetails.model.ActorMovieDetailsResponse
import com.london.data.datasource.remote.details.actordetails.model.ActorTvShowDetailsResponse

interface ActorDetailsRemoteDataSource {
    suspend fun getActorDetailsById(actorId: Int): ActorDetailsRemoteResponse
    suspend fun getActorMovieById(actorId: Int): ActorMovieDetailsResponse
    suspend fun getActorTvShowById(actorId: Int): ActorTvShowDetailsResponse
    suspend fun getActorImagePath(actorId: Int): String
}