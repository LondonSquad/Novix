package com.london.data.mapper.home.toprated

import com.london.data.remote.model.home.toprated.TopRatedTvSeriesRemote
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.shared.MediaType
import com.london.domain.entity.toprated.TopRatedMedia

fun TopRatedTvSeriesRemote.toEntity(): TopRatedMedia =
    TopRatedMedia(
        id = id.orZero(),
        name = name.orEmpty(),
        genres = genreIds.orEmpty().toGenre(MediaType.TvShow),
        posterUrl = posterPath.asImageUrlOrEmpty(),
        mediaType = MediaType.TvShow
    )
