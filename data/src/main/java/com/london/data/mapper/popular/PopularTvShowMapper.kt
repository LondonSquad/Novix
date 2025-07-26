@file:KoverIgnore

package com.london.data.mapper.popular

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.home.model.PopularTvShowResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.data.utils.roundToDecimal
import com.london.domain.KoverIgnore
import com.london.domain.entity.popular.PopularTvShow

fun PopularTvShowResponse.toEntity(): PopularTvShow {
    return PopularTvShow(
        id = id.orZero(),
        name = name.orEmpty(),
        overview = overview.orEmpty(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
        rating = voteAverage.orZero().roundToDecimal()
    )
}

fun ApiResponse<PopularTvShowResponse>.toEntityList(): List<PopularTvShow> =
    items.map { it.toEntity() }
