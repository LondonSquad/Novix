package com.london.data.mapper.toprated

import com.london.data.datasource.remote.toprated.movie.model.TopRatedMovieRemote
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.isTrue
import com.london.data.utils.orZero
import com.london.domain.entity.toprated.TopRatedMovie

fun TopRatedMovieRemote.toEntity(): TopRatedMovie {
    return TopRatedMovie(
        adult = adult.isTrue,
        backdropUrl = backdropPath.asImageUrlOrEmpty(),
        id = id.orZero(),
        originalLanguage = originalLanguage.orEmpty(),
        originalTitle = originalTitle.orEmpty(),
        overview = overview.orEmpty(),
        popularity = popularity.orZero(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
        releaseDate = releaseDate.orEmpty(),
        title = title.orEmpty(),
        video = video.isTrue,
        voteAverage = voteAverage.orZero(),
        voteCount = voteCount.orZero(),
        genreIds = genreIds.orEmpty()
    )
}