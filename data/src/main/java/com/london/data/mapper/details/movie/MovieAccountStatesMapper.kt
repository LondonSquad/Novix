package com.london.data.mapper.moviedetails

import com.london.data.remote.model.details.movie.model.moviedetails.AccountMovieStatesResponse
import com.london.data.utils.isTrue
import com.london.data.utils.orZero
import com.london.domain.entity.moviedatails.MovieStates

fun AccountMovieStatesResponse.toEntity() = MovieStates(
    favorite = favorite.isTrue,
    id = id.orZero(),
    rate = rated?.value.orZero(),
    watchlist = watchlist.isTrue
)