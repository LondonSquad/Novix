package com.london.domain.repository.myrating

import com.london.domain.entity.myrating.RatedMovie
import com.london.domain.entity.myrating.RatedTvShow

interface MyRatingRepository {
    suspend fun getAllRatedMovies(): List<RatedMovie>
    suspend fun getAllRatedTvShows(): List<RatedTvShow>
}