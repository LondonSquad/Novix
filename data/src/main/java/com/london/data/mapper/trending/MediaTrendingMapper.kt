package com.london.data.mapper.trending

import com.london.data.remote.model.trending.TrendingResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.trending.Trending

fun TrendingResponse.toMediaTrending(): Trending = Trending(
    id = id.orZero(),
    title = title.orEmpty(),
    posterPath = (posterPath ?: profilePath).asImageUrlOrEmpty(),
    genreIds = genreIds.orEmpty()
) 