@file:KoverIgnore

package com.london.data.mapper.recent

import com.london.data.local.model.recent.search.RecentSearchLocal
import com.london.domain.KoverIgnore
import com.london.domain.entity.recent.RecentSearch

fun RecentSearchLocal.toEntity(): RecentSearch = RecentSearch(
    query = query,
    timestamp = date
)

fun RecentSearch.toRecentSearch(): RecentSearchLocal = RecentSearchLocal(
    query = query,
    date = System.currentTimeMillis(),
)