@file:KoverIgnore
package com.london.data.mapper.recent

import com.london.data.datasource.local.model.recent.MediaTypeLocal
import com.london.data.datasource.local.model.recent.RecentViewedLocal
import com.london.domain.KoverIgnore
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.recent.RecentViewed

fun RecentViewedLocal.toEntity(): RecentViewed = RecentViewed(
    id = this.id, imageUrl = this.imageUrl, type = this.type.toEntity(), viewDate = this.viewDate
)

fun RecentViewed.toLocal(): RecentViewedLocal = RecentViewedLocal(
    id = this.id, imageUrl = this.imageUrl, type = this.type.toLocal(), viewDate = this.viewDate
)

fun MediaTypeLocal.toEntity(): MediaType =
    if (this == MediaTypeLocal.Movie) MediaType.Movie else MediaType.TvShow

fun MediaType.toLocal(): MediaTypeLocal =
    if (this == MediaType.Movie) MediaTypeLocal.Movie else MediaTypeLocal.TvShow

