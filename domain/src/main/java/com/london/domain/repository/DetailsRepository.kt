package com.london.domain.repository

import com.london.domain.entity.actordetails.ActorDetails
import com.london.domain.entity.actordetails.ActorImageDetails
import com.london.domain.entity.actordetails.ActorMovieDetails
import com.london.domain.entity.actordetails.ActorTvShowDetails
import com.london.domain.entity.tvshowdetails.TvShowCastEntity
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.entity.tvshowdetails.TvShowImagesEntity

interface DetailsRepository {
    suspend fun getTvShowDetailsById(tvShowId: Int): TvShowDetailsEntity
    suspend fun getCastTvShowById(tvShowId: Int): TvShowCastEntity
    suspend fun getImagesTvShowById(tvShowId: Int): TvShowImagesEntity
    suspend fun getActorDetailsById(actorId: Int): ActorDetails
    suspend fun getActorMoviePicksById(actorId: Int): ActorMovieDetails
    suspend fun getActorTvShowPicksById(actorId: Int): ActorTvShowDetails
    suspend fun getActorImagesById(actorId: Int): ActorImageDetails
}