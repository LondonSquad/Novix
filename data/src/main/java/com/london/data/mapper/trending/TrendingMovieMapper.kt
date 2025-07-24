package com.london.data.mapper.trending

import com.london.data.datasource.remote.home.trending.model.TrendingMovieDto
import com.london.domain.entity.trending.TrendingMovie
import com.london.data.utils.orZero
import com.london.data.utils.asImageUrlOrEmpty

fun TrendingMovieDto.toTrendingMovie(): TrendingMovie = TrendingMovie(
    id = id.orZero(),
    posterPath = posterPath.asImageUrlOrEmpty(),
    genreIds = genreIds ?: emptyList()
) 