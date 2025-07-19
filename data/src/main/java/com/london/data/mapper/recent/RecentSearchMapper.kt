@file:KoverIgnore
package com.london.data.mapper.recent

import com.london.data.datasource.local.model.recent.RecentSearchLocal
import com.london.domain.KoverIgnore
import com.london.domain.entity.recent.RecentSearch

fun RecentSearchLocal.toEntity(): RecentSearch=RecentSearch(
    id = this.id,
    query = this.query,
    timestamp = this.date
)

fun RecentSearch.toRecentSearch(): RecentSearchLocal= RecentSearchLocal(
    query = this.query,
    date = System.currentTimeMillis(),
    id = this.id
)