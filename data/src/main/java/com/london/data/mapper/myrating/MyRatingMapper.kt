package com.london.data.mapper.myrating

import com.london.data.remote.model.myrating.RatedMediaResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.RatedMedia

fun RatedMediaResponse.toEntity(
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
