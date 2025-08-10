package com.london.data.mapper.myrating

import com.london.data.remote.model.myrating.RatingMediaResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.RatedMedia

fun RatingMediaResponse.toEntity(
    isMovie: Boolean = true
): RatedMedia {
    return RatedMedia(
        id = id.orZero(),
        title = title.orEmpty(),
        posterPath = posterPath.asImageUrlOrEmpty(),
        rating = rating?.toInt().orZero(),
        isMovie = isMovie
    )
}
