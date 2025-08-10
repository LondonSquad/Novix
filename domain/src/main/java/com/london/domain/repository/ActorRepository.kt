package com.london.domain.repository

import com.london.domain.entity.actordetails.ActorDetails
import com.london.domain.entity.actordetails.ActorImageDetails
import com.london.domain.entity.actordetails.cast.CastDetails

interface ActorRepository {
    suspend fun getActorDetailsById(id: Int): ActorDetails
    suspend fun getActorMoviePicksById(id: Int): CastDetails
    suspend fun getActorTvShowPicksById(id: Int): CastDetails
    suspend fun getActorImagesById(id: Int): ActorImageDetails
}