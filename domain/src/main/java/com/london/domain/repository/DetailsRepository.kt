package com.london.domain.repository

import com.london.domain.entity.ActorDetails
import com.london.domain.entity.ActorMovieDetails
import com.london.domain.entity.ActorTvShowDetails
import com.london.domain.entity.tvshowdetails.CastEntity
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.entity.tvshowdetails.TvShowImagesEntity

interface DetailsRepository {
    suspend fun getTvShowDetailsById(tvShowId: Int): TvShowDetailsEntity
    suspend fun getCastTvShowById(tvShowId: Int): CastEntity
    suspend fun getImagesTvShowById(tvShowId: Int): TvShowImagesEntity
    suspend fun getActorDetailsById(actorId: Int): ActorDetails
    suspend fun getActorMovieById(actorId: Int): List<ActorMovieDetails>
    suspend fun getActorTvShowById(actorId: Int): List<ActorTvShowDetails>
}