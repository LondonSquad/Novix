package com.london.domain.repository

import com.london.domain.entity.actordetails.ActorDetails
import com.london.domain.entity.actordetails.actorimage.ActorImageDetails
import com.london.domain.entity.actordetails.actormovie.ActorMovieDetails
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowDetails

interface ActorRepository {
    suspend fun getActorDetailsById(id: Int): ActorDetails
    suspend fun getActorMoviePicksById(id: Int): ActorMovieDetails
    suspend fun getActorTvShowPicksById(id: Int): ActorTvShowDetails
    suspend fun getActorImagesById(id: Int): ActorImageDetails
}