package com.london.domain.repository

import com.london.domain.entity.actordetails.ActorDetails
import com.london.domain.entity.actordetails.actorimage.ActorImageDetails
import com.london.domain.entity.actordetails.actormovie.ActorMovieDetails
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowDetails

interface ActorRepository {
    suspend fun getActorDetailsById(actorId: Int): ActorDetails
    suspend fun getActorMoviePicksById(actorId: Int): ActorMovieDetails
    suspend fun getActorTvShowPicksById(actorId: Int): ActorTvShowDetails
    suspend fun getActorImagesById(actorId: Int): ActorImageDetails
}