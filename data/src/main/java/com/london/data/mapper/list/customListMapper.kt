package com.london.data.mapper.list

import com.london.data.remote.model.list.CustomMovieListResponse
import com.london.data.utils.orZero
import com.london.domain.entity.MovieList

fun CustomMovieListResponse.toEntity(): MovieList = MovieList(
    id = this.id.orZero().toUInt(),
    name = this.name.orEmpty(),
    moviesCount = this.itemCount.orZero().toUInt()
)