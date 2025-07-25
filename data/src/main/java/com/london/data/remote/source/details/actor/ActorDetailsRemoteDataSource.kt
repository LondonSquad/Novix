package com.london.data.remote.source.details.actor

import com.london.data.remote.model.details.actor.model.ActorDetailsResponse
import com.london.data.remote.model.details.actor.model.actorimage.ActorImageResponse
import com.london.data.remote.model.details.actor.model.actormoviedetails.ActorMovieDetailsResponse
import com.london.data.remote.model.details.actor.model.actortvshowdetails.ActorTvShowDetailsResponse

interface ActorDetailsRemoteDataSource {
    suspend fun getActorDetailsById(id: Int): Result<ActorDetailsResponse>
    suspend fun getActorMovieById(id: Int): Result<ActorMovieDetailsResponse>
    suspend fun getActorTvShowById(id: Int): Result<ActorTvShowDetailsResponse>
    suspend fun getActorImagePath(id: Int): Result<ActorImageResponse>
}