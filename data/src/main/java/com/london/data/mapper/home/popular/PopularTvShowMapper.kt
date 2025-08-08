@file:KoverIgnore

package com.london.data.mapper.home.popular

import com.london.data.local.model.home.popular.PopularSectionLocal
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.home.popular.PopularTvShowResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.data.utils.roundToDecimal
import com.london.domain.KoverIgnore
import com.london.domain.entity.popular.PopularTvShow
import com.london.domain.entity.recent.MediaType

fun PopularTvShowResponse.toMovieEntity(): PopularTvShow {
    return PopularTvShow(
        id = id.orZero(),
        name = name.orEmpty(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
        rating = voteAverage.orZero().roundToDecimal()
    )
}

fun ApiResponse<PopularTvShowResponse>.toPopularTvShows(): List<PopularTvShow> =
    items.map { it.toMovieEntity() }

fun PopularSectionLocal.toTvShowEntity(): PopularTvShow = PopularTvShow(
    id = id,
    name = name,
    posterUrl = posterPictureUrl,
    rating = rating,
)

fun PopularTvShow.toPopularTvShowSectionLocal(
    mediaType: MediaType,
    date: Long = System.currentTimeMillis()
): PopularSectionLocal = PopularSectionLocal(
    id = id,
    name = name,
    posterPictureUrl = posterUrl,
    rating = rating,
    mediaType = mediaType,
    date = date
)
