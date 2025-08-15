package com.london.data.mapper.home.toprated

import com.london.data.local.model.home.topRated.TopRatedLocal
import com.london.domain.entity.toprated.TopRatedMedia

fun TopRatedLocal.toEntity(): TopRatedMedia =
    TopRatedMedia(
        id = id,
        name = name,
        genreIds = genre,
        voteAverage = rating,
        mediaType = mediaType,
        releaseDate = releaseYear,
        posterUrl = posterPictureUrl,
    )
fun TopRatedMedia.toLocal(): TopRatedLocal =
    TopRatedLocal(
        id = id,
        name = name,
        genre = genreIds,
        rating = voteAverage,
        mediaType = mediaType,
        releaseYear = releaseDate,
        posterPictureUrl = posterUrl,
    )
