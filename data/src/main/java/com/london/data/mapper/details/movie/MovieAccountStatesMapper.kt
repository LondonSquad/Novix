package com.london.data.mapper.moviedetails

import com.london.data.remote.model.details.movie.model.moviedetails.AccountMovieStatesResponse
import com.london.data.utils.isTrue
import com.london.data.utils.orZero
import com.london.data.utils.parseRatingValue
import com.london.domain.entity.moviedatails.MovieStates


fun AccountMovieStatesResponse.toEntity() = MovieStates(
    favorite = favorite.isTrue,
    id = id.orZero(),
    rate  = rated.parseRatingValue()?.toInt().orZero(),
    watchlist = watchlist.isTrue
)

