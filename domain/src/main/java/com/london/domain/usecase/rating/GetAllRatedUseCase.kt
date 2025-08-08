package com.london.domain.usecase.rating

import com.london.domain.entity.myrating.RatedMovie
import com.london.domain.entity.myrating.RatedTvShow
import com.london.domain.repository.myrating.MyRatingRepository

class GetAllRatedUseCase(
    private val repository: MyRatingRepository
) {
    suspend fun invoke(): Pair<List<RatedTvShow>, List<RatedMovie>> {
        val ratedTvShows = repository.getAllRatedMTvShows()
        val ratedMovies = repository.getAllRatedMovies()
        return Pair(ratedTvShows, ratedMovies)
    }
}