package com.london.data.mapper.list

import com.london.data.local.model.customLists.MovieListLocal
import com.london.data.remote.model.list.CustomMovieListResponse
import com.london.data.utils.orZero
import com.london.domain.entity.MovieList

fun CustomMovieListResponse.toEntity(): MovieList = MovieList(
    id = this.id.orZero(),
    name = this.name.orEmpty(),
    moviesCount = this.itemCount.orZero()
)

fun CustomMovieListResponse.toLocal(): MovieListLocal =
    MovieListLocal(
        id = this.id.orZero(),
        name = this.name.orEmpty(),
        description = this.description.orEmpty(),
        itemCount = this.itemCount.orZero()
    )

fun MovieListLocal.toEntity(): MovieList =
    MovieList(
        id = this.id,
        name = this.name,
        moviesCount = this.itemCount
    )
