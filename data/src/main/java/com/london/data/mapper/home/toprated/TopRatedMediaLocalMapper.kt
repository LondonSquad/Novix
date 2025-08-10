package com.london.data.mapper.home.toprated

import com.london.data.local.model.home.topRated.TopRatedLocal
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.toprated.TopRatedMedia

fun TopRatedLocal.toEntity(): TopRatedMedia =
    TopRatedMedia(
        id = id.orZero(),
        posterUrl = posterPictureUrl.asImageUrlOrEmpty(),
        releaseDate = releaseYear,
        name = name,
        voteAverage = rating.orZero(),
        genreIds = genre
    )
fun TopRatedMedia.toLocal(): TopRatedLocal =
    TopRatedLocal(
        id = id,
        name = name,
        posterPictureUrl = posterUrl,
        rating = voteAverage,
        releaseYear = releaseDate,
        mediaType = MediaType.TvShow,
        genre = genreIds
    )
