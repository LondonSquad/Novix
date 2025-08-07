package com.london.domain.repository.myrating

import com.london.domain.entity.myrating.RatedMovie

interface MyRatingRepository {
    suspend fun getAllRatedMovies() : List<RatedMovie>
//    fun getAllRatedMTvShows()
}