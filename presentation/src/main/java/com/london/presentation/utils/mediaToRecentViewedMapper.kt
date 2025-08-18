package com.london.presentation.utils

import com.london.domain.entity.movie.Movie
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.recent.RecentViewed
import com.london.domain.entity.tvshow.TvShow

fun Movie.toRecentViewed(): RecentViewed=
     RecentViewed(
        id = this.id,
        imageUrl = this.posterUrl,
        type = MediaType.Movie,
        viewDate = System.currentTimeMillis()
    )


fun TvShow.toRecentViewed(): RecentViewed= RecentViewed(
    id = this.id,
    imageUrl = this.posterPicture,
    type = MediaType.TvShow,
    viewDate = System.currentTimeMillis()
)
