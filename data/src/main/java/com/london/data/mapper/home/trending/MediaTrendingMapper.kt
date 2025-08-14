package com.london.data.mapper.home.trending

import com.london.data.mapper.genre.toGenre
import com.london.data.remote.model.home.trending.TrendingResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.Trending
import com.london.domain.entity.recent.MediaType

fun TrendingResponse.toEntityMedia(): Trending = Trending(
    id = id.orZero(),
    title = (title ?: name).orEmpty(),
    posterPath = (posterPath ?: profilePath).asImageUrlOrEmpty(),
    genres = genreIds.orEmpty().map { it.toGenre(MediaType.Movie) }
)
