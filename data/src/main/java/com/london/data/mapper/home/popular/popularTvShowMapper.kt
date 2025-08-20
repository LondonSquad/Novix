package com.london.data.mapper.home.popular

import com.london.data.local.model.home.popular.PopularSectionLocal
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.popular.PopularTvShowResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.data.utils.roundToDecimal
import com.london.domain.entity.popular.PopularMedia
import com.london.domain.entity.shared.MediaType

fun PopularTvShowResponse.toTvShowEntity(): PopularMedia =
    PopularMedia(
        id = id.orZero(),
        name = name.orEmpty(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
        rating = voteAverage.orZero().roundToDecimal(),
        mediaType = MediaType.TvShow
    )

fun ApiResponse<PopularTvShowResponse>.toPopularTvShows(): List<PopularMedia> =
    items.map { it.toTvShowEntity() }

fun PopularSectionLocal.toTvShowEntity(): PopularMedia = PopularMedia(
    id = id,
    name = name,
    posterUrl = posterPictureUrl,
    rating = rating,
    mediaType = MediaType.TvShow
)

fun PopularMedia.toPopularTvShowSectionLocal(
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
