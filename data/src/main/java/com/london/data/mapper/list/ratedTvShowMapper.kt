package com.london.data.mapper.list

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.myrating.RatedTvShowResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.myrating.RatedTvShow

fun RatedTvShowResponse.toRatedTvShow(): RatedTvShow {
    return RatedTvShow(
        id = id.orZero(),
        title = name.orEmpty(),
        posterPath = posterPath.asImageUrlOrEmpty(),
        rating = rating?.toInt().orZero()
    )
}

fun ApiResponse<RatedTvShowResponse>.toEntity(): List<RatedTvShow> =
    items.map { it.toRatedTvShow() }