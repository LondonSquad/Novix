@file:KoverIgnore
package com.london.data.mapper.popular

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.home.popular.model.PopularTvShowResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.popular.PopularTvShow

fun PopularTvShowResponse.toPopularTvShow(): PopularTvShow{
    return PopularTvShow(
        id = id.orZero(),
        name = name,
        overview = overview,
        posterUrl = posterPath.asImageUrlOrEmpty(),
        rating = voteAverage
    )
}

fun ApiResponse<PopularTvShowResponse>.toPopularTvShows(): List<PopularTvShow> {
    return items.map { it.toPopularTvShow() }
}