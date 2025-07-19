package com.london.presentation.features.search

import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.recent.RecentViewed

fun Movie.toRecentViewed(): RecentViewed=
     RecentViewed(
        id = this.id,
        imageUrl = this.posterPicture,
        type = MediaType.Movie,
        viewDate = System.currentTimeMillis()
    )


fun TvShow.toRecentViewed(): RecentViewed= RecentViewed(
    id = this.id,
    imageUrl = this.posterPicture,
    type = MediaType.TvShow,
    viewDate = System.currentTimeMillis()
)
