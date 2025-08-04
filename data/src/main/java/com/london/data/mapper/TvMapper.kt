@file:KoverIgnore

package com.london.data.mapper

import com.london.data.remote.model.search.model.SearchTvShowRemote
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.extractYear
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.TvShow

fun SearchTvShowRemote.toEntity(): TvShow = TvShow(
    id = id.orZero(),
    name = name.orEmpty(),
    posterPicture = posterPath.asImageUrlOrEmpty(),
    releaseYear = firstAirDate.orEmpty().extractYear(),
    rating = voteAverage.orZero().toInt(),
    genres = genreIds.orEmpty()
)
