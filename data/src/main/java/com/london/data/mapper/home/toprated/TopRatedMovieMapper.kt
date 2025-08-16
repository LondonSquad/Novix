package com.london.data.mapper.home.toprated

import com.london.data.remote.model.home.toprated.TopRatedMovieRemote
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.toprated.TopRatedMedia

fun TopRatedMovieRemote.toEntity(): TopRatedMedia =
    TopRatedMedia(
        id = id.orZero(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
        name = title.orEmpty(),
        voteAverage = voteAverage.orZero(),
        genres = genreIds.orEmpty().toGenre(MediaType.Movie),
        mediaType = MediaType.Movie
    )
